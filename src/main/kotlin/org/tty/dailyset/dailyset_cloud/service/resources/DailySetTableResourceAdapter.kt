package org.tty.dailyset.dailyset_cloud.service.resources

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.tty.dailyset.dailyset_cloud.bean.DailySetUpdateItem
import org.tty.dailyset.dailyset_cloud.bean.entity.DailySetSourceLinks
import org.tty.dailyset.dailyset_cloud.bean.entity.DailySetTable
import org.tty.dailyset.dailyset_cloud.bean.enums.DailySetSourceType
import org.tty.dailyset.dailyset_cloud.mapper.DailySetSourceLinkMapper
import org.tty.dailyset.dailyset_cloud.mapper.DailySetTableMapper

@Component
class DailySetTableResourceAdapter: ResourceAdapter<DailySetTable> {

    @Autowired
    private lateinit var dailySetSourceLinkMapper: DailySetSourceLinkMapper

    @Autowired
    private lateinit var dailySetTableMapper: DailySetTableMapper

    override fun getUpdatedItems(dailySetUid: String, oldVersion: Int): List<DailySetUpdateItem<DailySetTable>> {
        val dailySetSourceLinks =
            dailySetSourceLinkMapper.findAllDailySetSourceLinkByDailySetUidAndSourceTypeAndVersionsLargerThan(
                dailySetUid,
                DailySetSourceType.Table.value,
                oldVersion
            )
        if (dailySetSourceLinks.isEmpty()) {
            return emptyList()
        }

        val dailySetTables = dailySetTableMapper.findAllDailyTableBySourceUidBatch(
            dailySetSourceLinks.map { it.sourceUid }
        )
        return join2Sources(dailySetSourceLinks, dailySetTables)
    }

    private fun join2Sources(
        dailySetSourceLinks: List<DailySetSourceLinks>,
        dailySetTables: List<DailySetTable>
    ): List<DailySetUpdateItem<DailySetTable>> {
        val dailySetTableMap = hashMapOf(*dailySetTables.map { it.sourceUid to it }.toTypedArray())

        return dailySetSourceLinks.map {
            DailySetUpdateItem(
                insertVersion = it.insertVersion,
                updateVersion = it.updateVersion,
                removeVersion = it.removeVersion,
                lastTick = it.lastTick,
                data = dailySetTableMap[it.sourceUid]!!
            )
        }
    }
}