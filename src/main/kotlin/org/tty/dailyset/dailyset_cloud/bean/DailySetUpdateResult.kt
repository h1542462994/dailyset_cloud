package org.tty.dailyset.dailyset_cloud.bean

import org.tty.dailyset.dailyset_cloud.bean.entity.DailySet

data class DailySetUpdateResult(
    val dailySet: DailySet,

    // 更新数据
    val updateItems: List<DailySetUpdateItemCollection<*>>
)