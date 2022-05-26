package org.tty.dailyset.dailyset_cloud.intent

import org.tty.dailyset.dailyset_cloud.bean.MessageIntent

/**
 * **intent** for [org.tty.dailyset.dailyset_cloud.service.MessageService.messagePostSystem]
 * and [org.tty.dailyset.dailyset_cloud.service.MessageService.messagePostTicket]
 */
class MessagePostIntent(
    val secret: String,
    val targets: List<String>,
    val intent: MessageIntent
): RequestIntent