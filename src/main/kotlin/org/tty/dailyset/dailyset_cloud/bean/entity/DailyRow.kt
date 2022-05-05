package org.tty.dailyset.dailyset_cloud.bean.entity

/**
 * **source-row(2)**
 */
data class DailyRow(
    val sourceUid: String,
    val tableUid: String,
    val currentIndex: Int,
    val weekdays: String,
    val counts: String
)