package org.tty.dailyset.dailyset_cloud.bean.entity

/**
 * **meta-usageMeta(2)**
 */
data class DailySetUsageMeta(
    val metaUid: String,
    val dailySetUid: String,
    val userUid: String,
    val authType: Int
)