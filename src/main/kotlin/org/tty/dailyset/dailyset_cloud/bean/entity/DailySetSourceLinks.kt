package org.tty.dailyset.dailyset_cloud.bean.entity

import org.tty.dailyset.dailyset_cloud.bean.UpdatableItemLink
import org.tty.dailyset.dailyset_cloud.bean.enums.DailySetSourceType
import org.tty.dailyset.dailyset_cloud.bean.serializer.LocalDateTimeSerializer
import java.time.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class DailySetSourceLinks(
    val dailySetUid: String,
    /**
     * @see DailySetSourceType
     */
    val sourceType: Int,
    val sourceUid: String,
    override val insertVersion: Int,
    override val updateVersion: Int,
    override val removeVersion: Int,
    @Serializable(with = LocalDateTimeSerializer::class)
    override val lastTick: LocalDateTime
): UpdatableItemLink