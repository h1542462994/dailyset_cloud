package org.tty.dailyset.dailyset_cloud.bean.entity

import org.tty.dailyset.dailyset_cloud.bean.enums.DailySetPeriodCode

@kotlinx.serialization.Serializable
data class DailySetCourse(
    val sourceUid: String,
    val year: Int,
    /**
     * @see DailySetPeriodCode
     */
    val periodCode: Int,
    val name: String,
    val campus: String,
    val location: String,
    val teacher: String,
    val weeks: String,
    val weekDay: Int,
    val sectionStart: Int,
    val sectionEnd: Int
)