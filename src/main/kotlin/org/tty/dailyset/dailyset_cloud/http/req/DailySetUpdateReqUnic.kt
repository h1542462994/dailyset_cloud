package org.tty.dailyset.dailyset_cloud.http.req

data class DailySetUpdateReqUnic(
    val ticketId: String,
    val uid: String,
    val type: Int,
    val sourceVersion: Int,
    val matteVersion: Int,
    val metaVersion: Int
)