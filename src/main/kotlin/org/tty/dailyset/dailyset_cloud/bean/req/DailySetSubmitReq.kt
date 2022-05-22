package org.tty.dailyset.dailyset_cloud.bean.req

import org.tty.dailyset.dailyset_cloud.http.resp.DailySetUpdateRawResult
import java.time.LocalDateTime

class DailySetSubmitReq(
    val submitItems: DailySetUpdateRawResult,
    val now: LocalDateTime
)