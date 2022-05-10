package org.tty.dailyset.dailyset_cloud.bean.req

import org.tty.dailyset.dailyset_cloud.bean.MessageIntent

class MessagePostSystemReq(
    val secret: String?,
    val targets: List<String>,
    val intent: MessageIntent?
) {
    fun verify(): Boolean {
        return secret != null && targets.isNotEmpty() && intent != null
    }
}