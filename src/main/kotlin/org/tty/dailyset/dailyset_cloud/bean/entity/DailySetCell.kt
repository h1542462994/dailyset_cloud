package org.tty.dailyset.dailyset_cloud.bean.entity

import org.tty.dailyset.dailyset_cloud.bean.serializer.LocalTimeSerializer
import java.time.LocalTime
import kotlinx.serialization.Serializable

/**
 * **source-cell(3)**
 */
@Serializable
data class DailySetCell(
    val sourceUid: String,
    val rowUid: String,
    val currentIndex: Int,
    @Serializable(with = LocalTimeSerializer::class)
    val startTime: LocalTime,
    @Serializable(with = LocalTimeSerializer::class)
    val endTime: LocalTime,
    val normalType: Int,
    val serialIndex: Int
)