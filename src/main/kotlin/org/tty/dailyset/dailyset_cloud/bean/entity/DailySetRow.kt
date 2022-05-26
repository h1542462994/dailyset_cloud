package org.tty.dailyset.dailyset_cloud.bean.entity
import kotlinx.serialization.Serializable

/**
 * **source-row(2)**
 */
@Serializable
data class DailySetRow(
    val sourceUid: String,
    val tableUid: String,
    val currentIndex: Int,
    val weekdays: String,
    val counts: String
)