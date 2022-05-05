package org.tty.dailyset.dailyset_cloud.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.tty.dailyset.dailyset_cloud.bean.DailySetUpdateItem
import org.tty.dailyset.dailyset_cloud.bean.DailySetUpdateItemCollection
import org.tty.dailyset.dailyset_cloud.bean.DailySetUpdateResult
import org.tty.dailyset.dailyset_cloud.bean.ResponseCodes
import org.tty.dailyset.dailyset_cloud.bean.entity.DailySet
import org.tty.dailyset.dailyset_cloud.bean.enums.DailySetDataType
import org.tty.dailyset.dailyset_cloud.bean.enums.DailySetSourceType
import org.tty.dailyset.dailyset_cloud.bean.enums.DailySetType
import org.tty.dailyset.dailyset_cloud.intent.DailySetUpdateIntent
import org.tty.dailyset.dailyset_cloud.mapper.DailySetMapper
import org.tty.dailyset.dailyset_cloud.mapper.DailySetUsageMetaMapper
import org.tty.dailyset.dailyset_cloud.service.resources.DailySetDurationResourceAdapter
import org.tty.dailyset.dailyset_cloud.service.school.SchoolAdapter

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

    /**
     * 获取所有Dailyset的数据.
     */
    suspend fun getDailysetInfos(userUid: Int): List<DailySet> {
        val resultList = mutableListOf<DailySet>()

        // 获取自动课表
        val resp = schoolAdapter.getSchoolDailyset(userUid)
        if (resp.code == ResponseCodes.success) {
            requireNotNull(resp.data)
            resultList.add(resp.data)
        }

        // 获取所有的使用元数据
        val dailySetUsageMetas = dailySetUsageMetaMapper.findAllDailySetUsageMetaByMetaUserUid(userUid)
        if (dailySetUsageMetas.isNotEmpty()) {
            val dailySets = dailySetMapper.findDailySetByUidBatch(
                dailySetUsageMetas.map { it.dailySetUid }.distinct()
            )
            resultList.addAll(dailySets)
        }

        return resultList
    }

    /**
     * 获取某个Dailyset的更新数据
     */
    suspend fun getUpdates(intent: DailySetUpdateIntent): DailySetUpdateResult? {
        val dailySet = dailySetMapper.findDailySetByUid(intent.dailySet.uid) ?: return null
        val uid = dailySet.uid

        val updateItems = mutableListOf<DailySetUpdateItemCollection<*>>()

        val dailySetDurationUpdateItems = DailySetUpdateItemCollection(
            type = DailySetDataType.Source.value,
            subType = DailySetSourceType.Duration.value,
            updates = dailySetDurationResourceAdapter.getUpdatedItems(uid, intent.dailySet.sourceVersion)
        )
        if (dailySetDurationUpdateItems.updates.isNotEmpty()) {
            updateItems.add(dailySetDurationUpdateItems)
        }

        return DailySetUpdateResult(
            dailySet,
            updateItems = updateItems
        )
    }


}