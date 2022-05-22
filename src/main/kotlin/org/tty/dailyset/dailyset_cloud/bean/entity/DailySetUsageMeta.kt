package org.tty.dailyset.dailyset_cloud.bean.entity

/**
 * **meta-usageMeta(2)**
 */
@kotlinx.serialization.Serializable
data class DailySetUsageMeta(
    val metaUid: String,
    val dailySetUid: String,
    val userUid: Int,
    val authType: Int
)