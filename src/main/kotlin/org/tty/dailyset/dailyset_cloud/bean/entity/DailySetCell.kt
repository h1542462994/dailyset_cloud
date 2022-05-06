package org.tty.dailyset.dailyset_cloud.bean.entity

import org.tty.dailyset.dailyset_cloud.bean.serializer.LocalDateTimeSerializer
import org.tty.dailyset.dailyset_cloud.bean.serializer.LocalTimeSerializer
import java.time.LocalTime

/**
 * **source-cell(3)**
 */
@kotlinx.serialization.Serializable
data class DailySetCell(
    val sourceUid: String,
    val rowUid: String,
    val currentIndex: Int,
    @kotlinx.serialization.Serializable(with = LocalTimeSerializer::class)
    val startTime: LocalTime,
    @kotlinx.serialization.Serializable(with = LocalTimeSerializer::class)
    val endTime: LocalTime,
    val normalType: Int,
    val serialIndex: Int
)