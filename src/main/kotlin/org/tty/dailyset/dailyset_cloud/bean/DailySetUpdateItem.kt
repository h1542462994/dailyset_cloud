package org.tty.dailyset.dailyset_cloud.bean

import java.time.LocalDateTime

data class DailySetUpdateItem<T: Any>(
    override val insertVersion: Int,

    override val updateVersion: Int,

    override val removeVersion: Int,

    override val lastTick: LocalDateTime,

    val data: T?
): UpdatableItemLink