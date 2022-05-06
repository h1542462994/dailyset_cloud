package org.tty.dailyset.dailyset_cloud.http.req

@kotlinx.serialization.Serializable
data class DailySetUpdateReqUnic(
    val ticketId: String,
    val uid: String,
    val type: Int,
    val sourceVersion: Int,
    val matteVersion: Int,
    val metaVersion: Int
)