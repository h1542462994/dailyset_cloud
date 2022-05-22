package org.tty.dailyset.dailyset_cloud.service.resources

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.tty.dailyset.dailyset_cloud.bean.DailySetUpdateItem
import org.tty.dailyset.dailyset_cloud.bean.entity.DailySetRow
import org.tty.dailyset.dailyset_cloud.bean.entity.DailySetSourceLinks
import org.tty.dailyset.dailyset_cloud.bean.enums.DailySetSourceType
import org.tty.dailyset.dailyset_cloud.mapper.DailySetRowMapper
import org.tty.dailyset.dailyset_cloud.mapper.DailySetSourceLinkMapper
import java.time.LocalDateTime

@Component
class DailySetRowResourceAdapter: ResourceAdapter<DailySetRow> {

    @Autowired
    private lateinit var dailySetSourceLinkMapper: DailySetSourceLinkMapper

    @Autowired
    private lateinit var dailySetRowMapper: DailySetRowMapper

    override fun getUpdatedItems(dailySetUid: String, oldVersion: Int): List<DailySetUpdateItem<DailySetRow>> {
        val dailySetSourceLinks =
            dailySetSourceLinkMapper.findAllByDailySetUidAndSourceTypeAndVersionsLargerThan(
                dailySetUid,
                DailySetSourceType.Row.value,
                oldVersion
            )
        if (dailySetSourceLinks.isEmpty()) {
            return emptyList()
        }

        val dailySetRows = dailySetRowMapper.findAllBySourceUid(
            dailySetSourceLinks.map { it.sourceUid }
        )
        return join2Sources(dailySetSourceLinks, dailySetRows)
    }

    override fun submitLocalChanges(
        dailySetUid: String,
        updateItems: List<DailySetUpdateItem<DailySetRow>>,
        now: LocalDateTime
    ) {
        TODO("Not yet implemented")
    }

    private fun join2Sources(
        dailySetSourceLinks: List<DailySetSourceLinks>,
        dailySetRows: List<DailySetRow>
    ): List<DailySetUpdateItem<DailySetRow>> {
        val dailySetRowMap = hashMapOf(*dailySetRows.map { it.sourceUid to it }.toTypedArray())

        return dailySetSourceLinks.map {
            DailySetUpdateItem(
                insertVersion = it.insertVersion,
                updateVersion = it.updateVersion,
                removeVersion = it.removeVersion,
                lastTick = it.lastTick,
                data = dailySetRowMap[it.sourceUid]!!
            )
        }
    }
}