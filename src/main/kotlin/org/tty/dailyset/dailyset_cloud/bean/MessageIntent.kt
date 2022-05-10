package org.tty.dailyset.dailyset_cloud.bean

data class MessageIntent(
    val topic: String,
    val referer: String,
    val code: Int,
    val content: String
)