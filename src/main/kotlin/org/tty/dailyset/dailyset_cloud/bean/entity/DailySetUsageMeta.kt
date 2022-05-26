package org.tty.dailyset.dailyset_cloud.bean.entity
import kotlinx.serialization.Serializable

/**
 * **meta-usageMeta(2)**
 */
@Serializable
data class DailySetUsageMeta(
    val metaUid: String,
    val dailySetUid: String,
    val userUid: String,
    val authType: Int
)