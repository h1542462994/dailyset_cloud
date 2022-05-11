package org.tty.dailyset.dailyset_cloud.bean.req

import org.tty.dailyset.dailyset_cloud.bean.MessageIntent

class MessagePostReq(
    val secret: String?,
    /**
     * the target user
     * if call by system, the content is **uid**
     * if call by ticket, the content is **ticket_id**
     */
    val targets: List<String>,
    val intent: MessageIntent?
) {
    fun verify(): Boolean {
        return secret != null && targets.isNotEmpty() && intent != null
    }
}