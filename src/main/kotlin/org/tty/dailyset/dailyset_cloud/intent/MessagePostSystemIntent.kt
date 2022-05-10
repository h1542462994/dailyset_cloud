package org.tty.dailyset.dailyset_cloud.intent

import org.tty.dailyset.dailyset_cloud.bean.MessageIntent

class MessagePostSystemIntent(
    val secret: String,
    val targets: List<String>,
    val intent: MessageIntent
)