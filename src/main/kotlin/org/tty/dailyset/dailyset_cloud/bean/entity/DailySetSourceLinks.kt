package org.tty.dailyset.dailyset_cloud.bean.entity

import org.tty.dailyset.dailyset_cloud.bean.UpdatableItemLink
import org.tty.dailyset.dailyset_cloud.bean.enums.DailySetSourceType
import java.time.LocalDateTime

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
    override val lastTick: LocalDateTime
): UpdatableItemLink