package org.tty.dailyset.dailyset_cloud.bean.entity
import kotlinx.serialization.Serializable

/**
 * **source-table(1)**
 */
@Serializable
data class DailySetTable(
    val sourceUid: String,
    val name: String
)