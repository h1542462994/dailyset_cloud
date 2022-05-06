package org.tty.dailyset.dailyset_cloud.bean.entity

/**
 * **source-row(2)**
 */
@kotlinx.serialization.Serializable
data class DailySetRow(
    val sourceUid: String,
    val tableUid: String,
    val currentIndex: Int,
    val weekdays: String,
    val counts: String
)