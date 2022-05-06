package org.tty.dailyset.dailyset_cloud.bean

import org.tty.dailyset.dailyset_cloud.bean.serializer.LocalDateTimeSerializer
import java.time.LocalDateTime

@kotlinx.serialization.Serializable

data class DailySetUpdateItem<T: Any>(
    override val insertVersion: Int,

    override val updateVersion: Int,

    override val removeVersion: Int,

    @kotlinx.serialization.Serializable(with = LocalDateTimeSerializer::class)
    override val lastTick: LocalDateTime,

    val data: T?
): UpdatableItemLink