package org.tty.dailyset.dailyset_cloud.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.tty.dailyset.dailyset_cloud.bean.*
import org.tty.dailyset.dailyset_cloud.bean.converters.toDailySetUpdateResultTrans
import org.tty.dailyset.dailyset_cloud.bean.entity.*
import org.tty.dailyset.dailyset_cloud.bean.enums.DailySetDataType
import org.tty.dailyset.dailyset_cloud.bean.enums.DailySetMetaType
import org.tty.dailyset.dailyset_cloud.bean.enums.DailySetSourceType
import org.tty.dailyset.dailyset_cloud.bean.enums.DailySetUsageAuth
import org.tty.dailyset.dailyset_cloud.http.DailySetUnicApi
import org.tty.dailyset.dailyset_cloud.http.req.DailySetUpdateReqUnic
import org.tty.dailyset.dailyset_cloud.intent.DailySetInfoIntent
import org.tty.dailyset.dailyset_cloud.intent.DailySetSubmitIntent
import org.tty.dailyset.dailyset_cloud.intent.DailySetUpdateIntent
import org.tty.dailyset.dailyset_cloud.mapper.DailySetMapper
import org.tty.dailyset.dailyset_cloud.mapper.DailySetUsageMetaMapper
import org.tty.dailyset.dailyset_cloud.mapper.UserTicketBindMapper
import org.tty.dailyset.dailyset_cloud.service.resources.*
import org.tty.dailyset.dailyset_cloud.service.school.SchoolAdapter
import org.tty.dailyset.dailyset_cloud.util.addNotNull
import java.util.Optional

@Component
class DailySetService {

    @Autowired
    private lateinit var schoolAdapter: SchoolAdapter

    @Autowired
    private lateinit var dailySetMapper: DailySetMapper

    @Autowired
    private lateinit var dailySetUsageMetaMapper: DailySetUsageMetaMapper

    @Autowired
    private lateinit var dailySetTableResourceAdapter: DailySetTableResourceAdapter

    @Autowired
    private lateinit var dailySetRowResourceAdapter: DailySetRowResourceAdapter

    @Autowired
    private lateinit var dailySetCellResourceAdapter: DailySetCellResourceAdapter

    @Autowired
    private lateinit var dailySetDurationResourceAdapter: DailySetDurationResourceAdapter

    @Autowired
    private lateinit var dailySetCourseResourceAdapter: DailySetCourseResourceAdapter

    @Autowired
    private lateinit var dailySetBasicMetaResourceAdapter: DailySetBasicMetaResourceAdapter

    @Autowired
    private lateinit var dailySetUsageMetaResourceAdapter: DailySetUsageMetaResourceAdapter

    @Autowired
    private lateinit var dailySetUnicApi: DailySetUnicApi

    @Autowired
    private lateinit var unicTicketBindMapper: UserTicketBindMapper

    /**
     * get the dailyset basic infos. a snapshot value for current resource.
     */
    suspend fun getDailysetInfos(intent: DailySetInfoIntent): Responses<List<DailySet>> {
        val resultList = mutableListOf<DailySet>()

        // ??????????????????
        val resp = schoolAdapter.getSchoolDailySets(intent.userUid)
        if (resp.code == ResponseCodes.success) {
            requireNotNull(resp.data)
            resultList.addAll(resp.data)
        }

        // ??????????????????????????????
        val dailySetUsageMetas = dailySetUsageMetaMapper.findAllByUserUid(intent.userUid)
        if (dailySetUsageMetas.isNotEmpty()) {
            val dailySets = dailySetMapper.findAllByUid(
                dailySetUsageMetas.map { it.dailySetUid }.distinct()
            )
            resultList.addAll(dailySets)
        }

        val distinctResultList = resultList.distinctBy { it.uid }
        return Responses.ok(code = resp.code, message = resp.message, data = distinctResultList)
    }

    /**
     * ????????????Dailyset???????????????
     */
    suspend fun getUpdates(intent: DailySetUpdateIntent): Responses<DailySetUpdateResult> {
        return if (UNIC_RESOURCES_REGEX.any {
                it.matches(intent.dailySet.uid)
            }) {
            getUpdatesOfAuto(intent)
        } else {
            getUpdatesOfLocal(intent)
        }
    }

    suspend fun submitLocalChanges(intent: DailySetSubmitIntent): Responses<Unit> {
        return if (UNIC_RESOURCES_REGEX.any {
                it.matches(intent.submitItems.dailySet.uid)
            }) {
            Responses.resourceReadonly()
        } else {
            val auth = findAuth(intent.userUid, intent.submitItems.dailySet) ?: return Responses.resourceNoAuth()
            doSubmitLocalChanges(intent, auth)
            Responses.ok()
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun doSubmitLocalChanges(intent: DailySetSubmitIntent, auth: DailySetUsageAuth) {
        intent.submitItems.updateItems.forEach {
            when(it.type) {
                DailySetDataType.Source.value -> {

                }
                DailySetDataType.Meta.value -> {
                    when(it.subType) {
                        DailySetMetaType.BasicMeta.value -> {
                            if (auth == DailySetUsageAuth.Owner) {
                                dailySetBasicMetaResourceAdapter.submitLocalChanges(intent.submitItems.dailySet.uid, it.updates as List<DailySetUpdateItem<DailySetBasicMeta>>, intent.now)
                            }
                        }
                    }
                }
            }
        }
    }

    private fun findAuth(userUid: String, dailySet: DailySet): DailySetUsageAuth? {
        val dailySetUsageMeta =
            dailySetUsageMetaMapper.findByDailySetUidAndUserUid(dailySetUid = dailySet.uid, userUid = userUid)
        return if (dailySetUsageMeta == null) {
            // the user hasn't any privilege.
            null
        } else {
            // get the auth.
            DailySetUsageAuth.of(dailySetUsageMeta.authType)
        }
    }


    /**
     * ???????????????????????????????????????????????????????????????????????????
     */
    private suspend fun getUpdatesOfAuto(intent: DailySetUpdateIntent): Responses<DailySetUpdateResult> {
        // ??????????????????
        val unicTicketBind = unicTicketBindMapper.findByUid(intent.userUid) ?: return Responses.ticketNotExist()

        // TODO: ?????????ZjutSchoolAdapter.
        val response = dailySetUnicApi.dailySetUpdate(
            DailySetUpdateReqUnic(
                ticketId = unicTicketBind.ticketId,
                uid = intent.dailySet.uid,
                type = intent.dailySet.type,
                sourceVersion = intent.dailySet.sourceVersion,
                matteVersion = intent.dailySet.matteVersion,
                metaVersion = intent.dailySet.metaVersion
            )
        )
        return if (response.code == ResponseCodes.success) {
            Responses.ok(
                data = response.data.toDailySetUpdateResultTrans()
            )
        } else {
            Responses.unicError()
        }
    }

    private suspend fun getUpdatesOfLocal(intent: DailySetUpdateIntent): Responses<DailySetUpdateResult> {
        val dailySet = dailySetMapper.findByUid(intent.dailySet.uid) ?: return Responses.resourceNoAuth()

        val updateItems = mutableListOf<DailySetUpdateItemCollection<*>>()

        //region sources data update
        updateItems.addNotNull(withDailySetTableUpdates(intent))
        updateItems.addNotNull(withDailySetRowUpdates(intent))
        updateItems.addNotNull(withDailySetCellUpdates(intent))
        updateItems.addNotNull(withDailySetDurationUpdates(intent))
        updateItems.addNotNull(withDailySetCourseUpdates(intent))
        updateItems.addNotNull(withDailySetBasicMetaUpdates(intent))
        updateItems.addNotNull(withDailySetUsageMetaUpdates(intent))
        //endregion

        return Responses.ok(data = DailySetUpdateResult(
            dailySet = dailySet,
            updateItems = updateItems
        )
        )
    }


    //region withDailySet...Updates
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

    private suspend fun withDailySetDurationUpdates(intent: DailySetUpdateIntent): DailySetUpdateItemCollection<DailySetDuration>? {
        val dailySetDurationUpdateItems = DailySetUpdateItemCollection(
            type = DailySetDataType.Source.value,
            subType = DailySetSourceType.Duration.value,
            updates = dailySetDurationResourceAdapter.getUpdatedItems(
                intent.dailySet.uid,
                intent.dailySet.sourceVersion
            )
        )
        return if (dailySetDurationUpdateItems.updates.isNotEmpty()) {
            dailySetDurationUpdateItems
        } else {
            null
        }
    }

    private suspend fun withDailySetCourseUpdates(intent: DailySetUpdateIntent): DailySetUpdateItemCollection<DailySetCourse>? {
        val dailySetCourseUpdateItems = DailySetUpdateItemCollection(
            type = DailySetDataType.Source.value,
            subType = DailySetSourceType.Course.value,
            updates = dailySetCourseResourceAdapter.getUpdatedItems(intent.dailySet.uid, intent.dailySet.sourceVersion)
        )
        return if (dailySetCourseUpdateItems.updates.isNotEmpty()) {
            dailySetCourseUpdateItems
        } else {
            null
        }
    }

    private suspend fun withDailySetBasicMetaUpdates(intent: DailySetUpdateIntent): DailySetUpdateItemCollection<DailySetBasicMeta>? {
        val dailySetBasicMetaItems = DailySetUpdateItemCollection(
            type = DailySetDataType.Meta.value,
            subType = DailySetMetaType.BasicMeta.value,
            updates = dailySetBasicMetaResourceAdapter.getUpdatedItems(intent.dailySet.uid, intent.dailySet.metaVersion)
        )
        return if (dailySetBasicMetaItems.updates.isNotEmpty()) {
            dailySetBasicMetaItems
        } else {
            null
        }
    }

    private suspend fun withDailySetUsageMetaUpdates(intent: DailySetUpdateIntent): DailySetUpdateItemCollection<DailySetUsageMeta>? {
        val dailySetUsageMetaItems = DailySetUpdateItemCollection(
            type = DailySetDataType.Meta.value,
            subType = DailySetMetaType.UsageMeta.value,
            updates = dailySetUsageMetaResourceAdapter.getUpdatedItems(intent.dailySet.uid, intent.dailySet.metaVersion)
        )
        return if (dailySetUsageMetaItems.updates.isNotEmpty()) {
            dailySetUsageMetaItems
        } else {
            null
        }
    }
    //endregion

    companion object {
        val UNIC_RESOURCES_REGEX = listOf(
            "^#school.zjut.course.[\\dA-Za-z_-]+$".toRegex(),
            "^#school.zjut.unic$".toRegex()
        )
    }
}