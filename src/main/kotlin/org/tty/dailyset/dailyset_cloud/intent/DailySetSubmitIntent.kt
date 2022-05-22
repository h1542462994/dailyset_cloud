package org.tty.dailyset.dailyset_cloud.intent

import org.tty.dailyset.dailyset_cloud.bean.DailySetUpdateResult
import java.time.LocalDateTime

class DailySetSubmitIntent(
    val userUid: Int,
    val submitItems: DailySetUpdateResult,
    val now: LocalDateTime
)