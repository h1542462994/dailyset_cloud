package org.tty.dailyset.dailyset_cloud.service.resources

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.tty.dailyset.dailyset_cloud.bean.DailySetUpdateItem
import org.tty.dailyset.dailyset_cloud.bean.entity.DailySetBasicMeta
import org.tty.dailyset.dailyset_cloud.bean.entity.EntityDefaults
import org.tty.dailyset.dailyset_cloud.bean.enums.DailySetMetaType
import org.tty.dailyset.dailyset_cloud.mapper.DailySetBasicMetaMapper
import org.tty.dailyset.dailyset_cloud.mapper.DailySetMapper
import org.tty.dailyset.dailyset_cloud.mapper.DailySetMetaLinksMapper
import org.tty.dailyset.dailyset_cloud.util.coerceLocalDateTime
import java.time.LocalDateTime

@Component
class DailySetBasicMetaResourceAdapter: ResourceAdapter<DailySetBasicMeta> {

    @Autowired
    private lateinit var dailySetMetaLinksMapper: DailySetMetaLinksMapper

    @Autowired
    private lateinit var dailySetBasicMetaMapper: DailySetBasicMetaMapper

    @Autowired
    private lateinit var dailySetMapper: DailySetMapper

    override fun getUpdatedItems(dailySetUid: String, oldVersion: Int): List<DailySetUpdateItem<DailySetBasicMeta>> {
        val metaLinks = dailySetMetaLinksMapper.findAllByDailySetUidAndMetaTypeAndVersionsLargerThan(
            dailySetUid = dailySetUid,
            metaType = DailySetMetaType.BasicMeta.value,
            oldVersion = oldVersion
        )
        if (metaLinks.isEmpty()) {
            return emptyList()
        }

        val metaLink = metaLinks[0]
        val dailySetBasicMeta = dailySetBasicMetaMapper.findByMetaUid(metaLink.metaUid)
        return listOf(
            DailySetUpdateItem(
                insertVersion = metaLink.insertVersion,
                updateVersion = metaLink.updateVersion,
                removeVersion = metaLink.removeVersion,
                lastTick = metaLink.lastTick,
                data = dailySetBasicMeta!!
            )
        )
    }

    override fun submitLocalChanges(dailySetUid: String, updateItems: List<DailySetUpdateItem<DailySetBasicMeta>>, now: LocalDateTime) {
        if (updateItems.isNotEmpty()) {
            val updateItem = updateItems.first()
            val dailySet = dailySetMapper.findByUid(dailySetUid) ?: return
            var metaVersion = dailySet.metaVersion
            if (updateItem.updateVersion == EntityDefaults.LOCAL_VERSION_ENABLE) {
                val existed = dailySetMetaLinksMapper.findByDailySetUidAndMetaTypeAndMetaUid(
                    dailySetUid = dailySetUid,
                    metaType = DailySetMetaType.BasicMeta.value,
                    metaUid = updateItem.data.metaUid
                )!!
                val tick = coerceLocalDateTime(updateItem.lastTick, now)

                if (existed.lastTick.isBefore(tick)) {
                   metaVersion += 1
                    dailySetMetaLinksMapper.update(
                        existed.copy(
                            updateVersion = metaVersion,
                            lastTick = tick
                        )
                    )
                    dailySetBasicMetaMapper.update(
                        updateItem.data
                    )
                }
                if (metaVersion != dailySet.metaVersion) {
                    dailySetMapper.update(dailySet.copy(metaVersion = metaVersion))
                }
            }

        }
    }
}