package org.tty.dailyset.dailyset_cloud.service.resources

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.tty.dailyset.dailyset_cloud.bean.DailySetUpdateItem
import org.tty.dailyset.dailyset_cloud.bean.entity.DailySetBasicMeta
import org.tty.dailyset.dailyset_cloud.bean.enums.DailySetMetaType
import org.tty.dailyset.dailyset_cloud.mapper.DailySetBasicMetaMapper
import org.tty.dailyset.dailyset_cloud.mapper.DailySetMetaLinksMapper

@Component
class DailySetBasicMetaResourceAdapter: ResourceAdapter<DailySetBasicMeta> {

    @Autowired
    private lateinit var dailySetMetaLinksMapper: DailySetMetaLinksMapper

    @Autowired
    private lateinit var dailySetBasicMetaMapper: DailySetBasicMetaMapper

    override fun getUpdatedItems(dailySetUid: String, oldVersion: Int): List<DailySetUpdateItem<DailySetBasicMeta>> {
        val metaLinks = dailySetMetaLinksMapper.findAllDailySetMetaLinkByDailySetUidAndSourceTypeAndVersionsLargerThan(
            dailySetUid = dailySetUid,
            metaType = DailySetMetaType.BasicMeta.value,
            oldVersion = oldVersion
        )
        if (metaLinks.isEmpty()) {
            return emptyList()
        }

        val metaLink = metaLinks[0]
        val dailySetBasicMeta = dailySetBasicMetaMapper.findDailySetBasicMetaByMetaUid(metaLink.metaUid)
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
}