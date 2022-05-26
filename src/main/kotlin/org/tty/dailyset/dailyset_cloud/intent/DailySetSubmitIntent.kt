package org.tty.dailyset.dailyset_cloud.intent

import org.tty.dailyset.dailyset_cloud.bean.DailySetUpdateResult
import java.time.LocalDateTime

/**
 * **intent** for [org.tty.dailyset.dailyset_cloud.service.DailySetService.submitLocalChanges]
 */
class DailySetSubmitIntent(
    val userUid: String,
    val submitItems: DailySetUpdateResult,
    val now: LocalDateTime
)