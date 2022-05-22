package org.tty.dailyset.dailyset_cloud.service.resources

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.tty.dailyset.dailyset_cloud.bean.DailySetUpdateItem
import org.tty.dailyset.dailyset_cloud.bean.entity.DailySetDuration
import org.tty.dailyset.dailyset_cloud.bean.entity.DailySetSourceLinks
import org.tty.dailyset.dailyset_cloud.bean.enums.DailySetSourceType
import org.tty.dailyset.dailyset_cloud.mapper.DailySetDurationMapper
import org.tty.dailyset.dailyset_cloud.mapper.DailySetSourceLinkMapper

@Component
class DailySetDurationResourceAdapter : ResourceAdapter<DailySetDuration> {
    @Autowired
    private lateinit var dailySetSourceLinkMapper: DailySetSourceLinkMapper

    @Autowired
    private lateinit var dailySetDurationMapper: DailySetDurationMapper

    override fun getUpdatedItems(dailySetUid: String, oldVersion: Int): List<DailySetUpdateItem<DailySetDuration>> {
        val dailySetSourceLinks =
            dailySetSourceLinkMapper.findAllDailySetSourceLinkByDailySetUidAndSourceTypeAndVersionsLargerThan(
                dailySetUid,
                DailySetSourceType.Duration.value,
                oldVersion
            )
        if (dailySetSourceLinks.isEmpty()) {
            return emptyList()
        }

        val dailySetDurations = dailySetDurationMapper.findAllDailySetDurationBySourceUidBatch(
            dailySetSourceLinks.map { it.sourceUid }
        )
        return join2Sources(dailySetSourceLinks, dailySetDurations)
    }

    private fun join2Sources(
        dailySetSourceLinks: List<DailySetSourceLinks>,
        dailySetDurations: List<DailySetDuration>
    ): List<DailySetUpdateItem<DailySetDuration>> {
        val dailySetDurationsMap = hashMapOf(*dailySetDurations.map { it.sourceUid to it }.toTypedArray())

        return dailySetSourceLinks.map {
            DailySetUpdateItem(
                insertVersion = it.insertVersion,
                updateVersion = it.updateVersion,
                removeVersion = it.removeVersion,
                lastTick = it.lastTick,
                data = dailySetDurationsMap[it.sourceUid]!!
            )
        }
    }
}