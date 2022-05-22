package org.tty.dailyset.dailyset_cloud.service.resources

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.tty.dailyset.dailyset_cloud.bean.DailySetUpdateItem
import org.tty.dailyset.dailyset_cloud.bean.entity.DailySetMetaLinks
import org.tty.dailyset.dailyset_cloud.bean.entity.DailySetUsageMeta
import org.tty.dailyset.dailyset_cloud.bean.enums.DailySetMetaType
import org.tty.dailyset.dailyset_cloud.mapper.DailySetMetaLinksMapper
import org.tty.dailyset.dailyset_cloud.mapper.DailySetUsageMetaMapper
import java.time.LocalDateTime

@Component
class DailySetUsageMetaResourceAdapter: ResourceAdapter<DailySetUsageMeta> {

    @Autowired
    private lateinit var dailySetMetaLinkMapper: DailySetMetaLinksMapper

    @Autowired
    private lateinit var dailySetUsageMetaMapper: DailySetUsageMetaMapper

    override fun getUpdatedItems(dailySetUid: String, oldVersion: Int): List<DailySetUpdateItem<DailySetUsageMeta>> {
        val dailySetMetaLinks =
            dailySetMetaLinkMapper.findAllByDailySetUidAndMetaTypeAndVersionsLargerThan(
                dailySetUid,
                DailySetMetaType.UsageMeta.value,
                oldVersion
            )
        if (dailySetMetaLinks.isEmpty()) {
            return emptyList()
        }

        val dailySetUsageMetas = dailySetUsageMetaMapper.findAllByMetaUid(
            dailySetMetaLinks.map { it.metaUid }
        )
        return join2Sources(dailySetMetaLinks, dailySetUsageMetas)
    }

    override fun submitLocalChanges(
        dailySetUid: String,
        updateItems: List<DailySetUpdateItem<DailySetUsageMeta>>,
        now: LocalDateTime
    ) {
        TODO("Not yet implemented")
    }

    private fun join2Sources(
        dailySetMetaLinks: List<DailySetMetaLinks>,
        dailySetUsageMetas: List<DailySetUsageMeta>
    ): List<DailySetUpdateItem<DailySetUsageMeta>> {
        val dailySetUsageMetaMap = hashMapOf(*dailySetUsageMetas.map { it.metaUid to it }.toTypedArray())

        return dailySetMetaLinks.map {
            DailySetUpdateItem(
                insertVersion = it.insertVersion,
                updateVersion = it.updateVersion,
                removeVersion = it.removeVersion,
                lastTick = it.lastTick,
                data = dailySetUsageMetaMap[it.metaUid]!!
            )
        }
    }
}