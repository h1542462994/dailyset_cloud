package org.tty.dailyset.dailyset_cloud.bean.entity

@kotlinx.serialization.Serializable
data class DailySetSchoolInfoMeta(
    val metaUid: String,
    val identifier: String,
    val name: String
)