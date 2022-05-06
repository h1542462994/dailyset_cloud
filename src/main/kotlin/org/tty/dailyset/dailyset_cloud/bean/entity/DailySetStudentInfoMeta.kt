package org.tty.dailyset.dailyset_cloud.bean.entity

@kotlinx.serialization.Serializable
data class DailySetStudentInfoMeta(
    val metaUid: String,
    val departmentName: String,
    val className: String,
    val name: String,
    val grade: Int
)