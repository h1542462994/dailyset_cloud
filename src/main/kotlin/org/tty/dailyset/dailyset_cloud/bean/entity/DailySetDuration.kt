package org.tty.dailyset.dailyset_cloud.bean.entity

import java.time.LocalDate
import org.tty.dailyset.dailyset_cloud.bean.enums.DailySetDurationType
import org.tty.dailyset.dailyset_cloud.bean.enums.DailySetPeriodCode
import org.tty.dailyset.dailyset_cloud.bean.serializer.LocalDateSerializer

@kotlinx.serialization.Serializable
data class DailySetDuration(
    val sourceUid: String,
    /**
     * @see DailySetDurationType
     */
    val type: Int,
    @kotlinx.serialization.Serializable(with = LocalDateSerializer::class)
    val startDate: LocalDate,
    @kotlinx.serialization.Serializable(with = LocalDateSerializer::class)
    val endDate: LocalDate,
    val name: String,
    val tag: String,
    val bindingYear: Int,
    /**
     * @see DailySetPeriodCode
     */
    val bindingPeriodCode: Int
)