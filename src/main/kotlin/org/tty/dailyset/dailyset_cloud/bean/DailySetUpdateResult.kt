package org.tty.dailyset.dailyset_cloud.bean

import org.tty.dailyset.dailyset_cloud.bean.entity.DailySet

data class DailySetUpdateResult(
    val dailySet: DailySet,

    /**
     * updateItems
     */
    val updateItems: List<DailySetUpdateItemCollection<*>>
)