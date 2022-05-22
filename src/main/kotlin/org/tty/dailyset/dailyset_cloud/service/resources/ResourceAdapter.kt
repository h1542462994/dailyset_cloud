package org.tty.dailyset.dailyset_cloud.service.resources

import org.tty.dailyset.dailyset_cloud.bean.DailySetUpdateItem
import java.time.LocalDateTime

interface ResourceAdapter<T: Any> {


    fun getUpdatedItems(dailySetUid: String, oldVersion: Int): List<DailySetUpdateItem<T>>

    fun submitLocalChanges(dailySetUid: String, updateItems: List<DailySetUpdateItem<T>>, now: LocalDateTime)
}