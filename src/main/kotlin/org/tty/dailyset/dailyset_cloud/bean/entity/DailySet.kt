package org.tty.dailyset.dailyset_cloud.bean.entity

@kotlinx.serialization.Serializable
data class DailySet(
    val uid: String,
    val type: Int,
    val sourceVersion: Int,
    val matteVersion: Int,
    val metaVersion: Int
)