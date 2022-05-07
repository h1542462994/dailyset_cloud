package org.tty.dailyset.dailyset_cloud.bean.entity

import java.time.LocalDate
import org.tty.dailyset.dailyset_cloud.bean.enums.DailySetDurationType
import org.tty.dailyset.dailyset_cloud.bean.enums.DailySetPeriodCode
import org.tty.dailyset.dailyset_cloud.bean.serializer.LocalDateSerializer
import kotlinx.serialization.Serializable

@Serializable
data class DailySetDuration(
    val sourceUid: String,
    /**
     * @see DailySetDurationType
     */
    val type: Int,
    @Serializable(with = LocalDateSerializer::class)
    val startDate: LocalDate,
    @Serializable(with = LocalDateSerializer::class)
    val endDate: LocalDate,
    val name: String,
    val tag: String,
    val bindingYear: Int,
    /**
     * @see DailySetPeriodCode
     */
    val bindingPeriodCode: Int
)