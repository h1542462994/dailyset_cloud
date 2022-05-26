package org.tty.dailyset.dailyset_cloud.bean.entity
import kotlinx.serialization.Serializable

@Serializable
data class DailySet(
    val uid: String,
    val type: Int,
    val sourceVersion: Int,
    val matteVersion: Int,
    val metaVersion: Int
)