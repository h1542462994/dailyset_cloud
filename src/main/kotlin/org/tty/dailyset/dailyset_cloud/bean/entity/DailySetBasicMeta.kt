package org.tty.dailyset.dailyset_cloud.bean.entity
import kotlinx.serialization.Serializable

/**
 * **meta-basicMeta(1)**
 */
@Serializable
data class DailySetBasicMeta(
    val metaUid: String,
    val name: String,
    val icon: String
)