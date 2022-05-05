package org.tty.dailyset.dailyset_cloud.service.resources

import org.springframework.stereotype.Component
import org.tty.dailyset.dailyset_cloud.bean.DailySetUpdateItem
import org.tty.dailyset.dailyset_cloud.bean.entity.DailyTable

@Component
class DailyTableResourceAdapter: ResourceAdapter<DailyTable> {

    override fun getUpdatedItems(dailySetUid: String, oldVersion: Int): List<DailySetUpdateItem<DailyTable>> {
       TODO()
    }
}