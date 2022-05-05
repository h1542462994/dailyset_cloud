package org.tty.dailyset.dailyset_cloud.service.resources

import org.tty.dailyset.dailyset_cloud.bean.DailySetUpdateItem

interface ResourceAdapter<T: Any> {
    fun getUpdatedItems(dailySetUid: String, oldVersion: Int): List<DailySetUpdateItem<T>>
}