package org.tty.dailyset.dailyset_cloud.intent

import org.tty.dailyset.dailyset_cloud.bean.entity.DailySet

class DailySetUpdateIntent(
    val userUid: Int,
    val dailySet: DailySet
)