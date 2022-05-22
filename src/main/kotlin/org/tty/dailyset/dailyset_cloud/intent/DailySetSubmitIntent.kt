package org.tty.dailyset.dailyset_cloud.intent

import org.tty.dailyset.dailyset_cloud.bean.DailySetUpdateResult

class DailySetSubmitIntent(
    val userUid: Int,
    val submitItems: DailySetUpdateResult
)