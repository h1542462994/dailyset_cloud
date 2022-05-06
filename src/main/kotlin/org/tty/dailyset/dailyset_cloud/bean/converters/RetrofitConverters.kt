package org.tty.dailyset.dailyset_cloud.bean.converters

import org.tty.dailyset.dailyset_cloud.bean.DailySetUpdateResult
import org.tty.dailyset.dailyset_cloud.http.resp.DailySetUpdateRawResult

fun DailySetUpdateRawResult?.toDailysetUpdateResultNoTrans(): DailySetUpdateResult? {
    if (this == null) {
        return null
    }

    return DailySetUpdateResult(
        dailySet = this.dailySet,
        updateItems = this.updateItems
    )
}