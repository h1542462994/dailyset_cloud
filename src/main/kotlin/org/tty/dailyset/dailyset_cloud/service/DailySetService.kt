package org.tty.dailyset.dailyset_cloud.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.tty.dailyset.dailyset_cloud.bean.DailySetUpdateItemCollection
import org.tty.dailyset.dailyset_cloud.bean.DailySetUpdateResult
import org.tty.dailyset.dailyset_cloud.bean.ResponseCodes
import org.tty.dailyset.dailyset_cloud.bean.Responses
import org.tty.dailyset.dailyset_cloud.bean.converters.toDailySetUpdateResultTrans
import org.tty.dailyset.dailyset_cloud.bean.converters.toDailysetUpdateResultNoTrans
import org.tty.dailyset.dailyset_cloud.bean.entity.*
import org.tty.dailyset.dailyset_cloud.bean.enums.DailySetDataType
import org.tty.dailyset.dailyset_cloud.bean.enums.DailySetMetaType
import org.tty.dailyset.dailyset_cloud.bean.enums.DailySetSourceType
import org.tty.dailyset.dailyset_cloud.bean.enums.DailySetType
import org.tty.dailyset.dailyset_cloud.http.DailySetUnicApi
import org.tty.dailyset.dailyset_cloud.http.req.DailySetUpdateReqUnic
import org.tty.dailyset.dailyset_cloud.intent.DailySetUpdateIntent
import org.tty.dailyset.dailyset_cloud.mapper.DailySetMapper
import org.tty.dailyset.dailyset_cloud.mapper.DailySetUsageMetaMapper
import org.tty.dailyset.dailyset_cloud.mapper.UserTicketBindMapper
import org.tty.dailyset.dailyset_cloud.service.resources.*
import org.tty.dailyset.dailyset_cloud.service.school.SchoolAdapter
import org.tty.dailyset.dailyset_cloud.util.addNotNull

@Component
class DailySetService {

    @Autowired
    private lateinit var schoolAdapter: SchoolAdapter

    @Autowired
    private lateinit var dailySetMapper: DailySetMapper

    @Autowired
    private lateinit var dailySetUsageMetaMapper: DailySetUsageMetaMapper

    @Autowired
    private lateinit var dailySetDurationResourceAdapter: DailySetDurationResourceAdapter

    @Autowired
    private lateinit var dailySetTableResourceAdapter: DailySetTableResourceAdapter

    @Autowired
    private lateinit var dailySetRowResourceAdapter: DailySetRowResourceAdapter

    @Autowired
    private lateinit var dailySetCellResourceAdapter: DailySetCellResourceAdapter

    @Autowired
    private lateinit var dailySetBasicMetaResourceAdapter: DailySetBasicMetaResourceAdapter

    @Autowired
    private lateinit var dailySetUnicApi: DailySetUnicApi

    @Autowired
    private lateinit var unicTicketBindMapper: UserTicketBindMapper

    /**
     * 获取所有Dailyset的数据.
     */
    suspend fun getDailysetInfos(userUid: Int): Responses<List<DailySet>>  {
        val resultList = mutableListOf<DailySet>()

        // 获取自动课表
        val resp = schoolAdapter.getSchoolDailySets(userUid)
        if (resp.code == ResponseCodes.success) {
            requireNotNull(resp.data)
            resultList.addAll(resp.data)
        }

        // 获取所有的使用元数据
        val dailySetUsageMetas = dailySetUsageMetaMapper.findAllDailySetUsageMetaByMetaUserUid(userUid)
        if (dailySetUsageMetas.isNotEmpty()) {
            val dailySets = dailySetMapper.findDailySetByUidBatch(
                dailySetUsageMetas.map { it.dailySetUid }.distinct()
            )
            resultList.addAll(dailySets)
        }

        return Responses.ok(code = resp.code, message = resp.message, data = resultList)
    }

    /**
     * 获取某个Dailyset的更新数据
     */
    suspend fun getUpdates(intent: DailySetUpdateIntent): DailySetUpdateResult? {
        val autoDailySetUids = listOf(
            "^#school.zjut.course.[\\dA-Za-z_-]+$".toRegex(),
            "^#school.zjut.unic$".toRegex()
        )
        return if (autoDailySetUids.any {
                it.matches(intent.dailySet.uid)
            }) {
            getUpdatesOfAuto(intent)
        } else {
            getUpdatesOfLocal(intent)
        }
    }

    /**
     * 获取自动课表的更新数据，这需要请求其他的应用服务器
     */
    private suspend fun getUpdatesOfAuto(intent: DailySetUpdateIntent): DailySetUpdateResult? {
        // 获取自动课表
        val unicTicketBind = unicTicketBindMapper.findUserTicketBindByUid(intent.userUid) ?: return null

        // TODO: 迁移到ZjutSchoolAdapter.
        val response = dailySetUnicApi.dailySetUpdate(DailySetUpdateReqUnic(
            ticketId = unicTicketBind.ticketId,
            uid = intent.dailySet.uid,
            type = intent.dailySet.type,
            sourceVersion = intent.dailySet.sourceVersion,
            matteVersion = intent.dailySet.matteVersion,
            metaVersion = intent.dailySet.metaVersion
        ))
        return if (response.code == ResponseCodes.success) {
            response.data.toDailySetUpdateResultTrans()
        } else {
            null
        }
    }

    private suspend fun getUpdatesOfLocal(intent: DailySetUpdateIntent): DailySetUpdateResult? {
        val dailySet = dailySetMapper.findDailySetByUid(intent.dailySet.uid) ?: return null

        val updateItems = mutableListOf<DailySetUpdateItemCollection<*>>()

        //region sources data update
        updateItems.addNotNull(withDailySetDurationUpdates(intent))
        updateItems.addNotNull(withDailySetTableUpdates(intent))
        updateItems.addNotNull(withDailySetRowUpdates(intent))
        updateItems.addNotNull(withDailySetCellUpdates(intent))
        updateItems.addNotNull(withDailySetBasicMetaUpdates(intent))
        //endregion

        return DailySetUpdateResult(
            dailySet,
            updateItems = updateItems
        )
    }

    private suspend fun withDailySetDurationUpdates(intent: DailySetUpdateIntent): DailySetUpdateItemCollection<DailySetDuration>? {
        val dailySetDurationUpdateItems = DailySetUpdateItemCollection(
            type = DailySetDataType.Source.value,
            subType = DailySetSourceType.Duration.value,
            updates = dailySetDurationResourceAdapter.getUpdatedItems(intent.dailySet.uid, intent.dailySet.sourceVersion)
        )
        return if (dailySetDurationUpdateItems.updates.isNotEmpty()) {
            dailySetDurationUpdateItems
        } else {
            null
        }
    }

    private suspend fun withDailySetTableUpdates(intent: DailySetUpdateIntent): DailySetUpdateItemCollection<DailySetTable>? {
        val dailySetTableUpdateItems = DailySetUpdateItemCollection(
            type = DailySetDataType.Source.value,
            subType = DailySetSourceType.Table.value,
            updates = dailySetTableResourceAdapter.getUpdatedItems(intent.dailySet.uid, intent.dailySet.sourceVersion)
        )
        return if (dailySetTableUpdateItems.updates.isNotEmpty()) {
            dailySetTableUpdateItems
        } else {
            null
        }
    }

    private suspend fun withDailySetRowUpdates(intent: DailySetUpdateIntent): DailySetUpdateItemCollection<DailySetRow>? {
        val dailySetRowUpdateItems = DailySetUpdateItemCollection(
            type = DailySetDataType.Source.value,
            subType = DailySetSourceType.Row.value,
            updates = dailySetRowResourceAdapter.getUpdatedItems(intent.dailySet.uid, intent.dailySet.sourceVersion)
        )
        return if (dailySetRowUpdateItems.updates.isNotEmpty()) {
            dailySetRowUpdateItems
        } else {
            null
        }
    }

    private suspend fun withDailySetCellUpdates(intent: DailySetUpdateIntent): DailySetUpdateItemCollection<DailySetCell>? {
        val dailySetCellUpdateItems = DailySetUpdateItemCollection(
            type = DailySetDataType.Source.value,
            subType = DailySetSourceType.Cell.value,
            updates = dailySetCellResourceAdapter.getUpdatedItems(intent.dailySet.uid, intent.dailySet.sourceVersion)
        )
        return if (dailySetCellUpdateItems.updates.isNotEmpty()) {
            dailySetCellUpdateItems
        } else {
            null
        }
    }

    private suspend fun withDailySetBasicMetaUpdates(intent: DailySetUpdateIntent): DailySetUpdateItemCollection<DailySetBasicMeta>? {
        val dailySetBasicMetaItems = DailySetUpdateItemCollection(
            type = DailySetDataType.Meta.value,
            subType = DailySetMetaType.BasicMeta.value,
            updates = dailySetBasicMetaResourceAdapter.getUpdatedItems(intent.dailySet.uid, intent.dailySet.sourceVersion)
        )
        return if (dailySetBasicMetaItems.updates.isNotEmpty()) {
            dailySetBasicMetaItems
        } else {
            null
        }
    }

}