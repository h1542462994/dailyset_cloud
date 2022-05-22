package org.tty.dailyset.dailyset_cloud.service.resources

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.tty.dailyset.dailyset_cloud.bean.DailySetUpdateItem
import org.tty.dailyset.dailyset_cloud.bean.entity.DailySetCell
import org.tty.dailyset.dailyset_cloud.bean.entity.DailySetSourceLinks
import org.tty.dailyset.dailyset_cloud.bean.enums.DailySetSourceType
import org.tty.dailyset.dailyset_cloud.mapper.DailySetCellMapper
import org.tty.dailyset.dailyset_cloud.mapper.DailySetSourceLinkMapper
import java.time.LocalDateTime

@Component
class DailySetCellResourceAdapter: ResourceAdapter<DailySetCell> {

    @Autowired
    private lateinit var dailySetSourceLinkMapper: DailySetSourceLinkMapper

    @Autowired
    private lateinit var dailySetCellMapper: DailySetCellMapper

    override fun getUpdatedItems(dailySetUid: String, oldVersion: Int): List<DailySetUpdateItem<DailySetCell>> {
        val dailySetSourceLinks =
            dailySetSourceLinkMapper.findAllByDailySetUidAndSourceTypeAndVersionsLargerThan(
                dailySetUid,
                DailySetSourceType.Cell.value,
                oldVersion
            )
        if (dailySetSourceLinks.isEmpty()) {
            return emptyList()
        }

        val dailySetCells = dailySetCellMapper.findAllBySourceUid(
            dailySetSourceLinks.map { it.sourceUid }
        )
        return join2Sources(dailySetSourceLinks, dailySetCells)
    }

    override fun submitLocalChanges(
        dailySetUid: String,
        updateItems: List<DailySetUpdateItem<DailySetCell>>,
        now: LocalDateTime
    ) {
        TODO("Not yet implemented")
    }

    private fun join2Sources(
        dailySetSourceLinks: List<DailySetSourceLinks>,
        dailySetCells : List<DailySetCell>
    ): List<DailySetUpdateItem<DailySetCell>> {
        val dailySetCellMap = hashMapOf(*dailySetCells.map { it.sourceUid to it }.toTypedArray())

        return dailySetSourceLinks.map {
            DailySetUpdateItem(
                insertVersion = it.insertVersion,
                updateVersion = it.updateVersion,
                removeVersion = it.removeVersion,
                lastTick = it.lastTick,
                data = dailySetCellMap[it.sourceUid]!!
            )
        }
    }
}