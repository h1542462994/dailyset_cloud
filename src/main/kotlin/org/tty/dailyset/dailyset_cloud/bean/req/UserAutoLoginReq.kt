package org.tty.dailyset.dailyset_cloud.bean.req

import org.tty.dailyset.dailyset_cloud.util.anyTextEmpty

class UserAutoLoginReq(
    val token: String? = null
) {
    fun verify(): Boolean {
        return !anyTextEmpty(token)
    }
}