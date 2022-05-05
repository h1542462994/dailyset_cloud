package org.tty.dailyset.dailyset_cloud.bean.entity

import java.time.LocalTime

/**
 * **source-cell(3)**
 */
data class DailySetCell(
    val sourceUid: String,
    val rowUid: String,
    val currentIndex: Int,
    val startTime: LocalTime,
    val endTime: LocalTime,
    val normalType: Int,
    val serialIndex: Int
)