package org.tty.dailyset.dailyset_cloud.bean.entity
import kotlinx.serialization.Serializable

@Serializable
data class DailySetSchoolInfoMeta(
    val metaUid: String,
    val identifier: String,
    val name: String
)