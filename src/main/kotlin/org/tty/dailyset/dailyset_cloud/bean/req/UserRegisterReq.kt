package org.tty.dailyset.dailyset_cloud.bean.req

import org.tty.dailyset.dailyset_cloud.util.anyEmpty

class UserRegisterReq(
    val nickname: String? = null,
    val password: String? = null,
    val email: String? = null,
    val portraitId: String? = null,
) {
    fun verify(): Boolean =
        !anyEmpty(nickname, password, email)
}