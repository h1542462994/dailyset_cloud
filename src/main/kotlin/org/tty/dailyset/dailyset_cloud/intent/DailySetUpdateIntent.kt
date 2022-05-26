package org.tty.dailyset.dailyset_cloud.intent

import org.tty.dailyset.dailyset_cloud.bean.entity.DailySet

/**
 * **intent** for [org.tty.dailyset.dailyset_cloud.service.DailySetService.getUpdates]
 */
class DailySetUpdateIntent(
    val userUid: String,
    val dailySet: DailySet
): RequestIntent