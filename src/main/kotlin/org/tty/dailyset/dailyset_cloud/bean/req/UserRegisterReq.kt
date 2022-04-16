/**
 * create at 2022/4/15
 * author h1542462994
 *
 * request bean class UserRegisterReq
 */

package org.tty.dailyset.dailyset_cloud.bean.req

import org.tty.dailyset.dailyset_cloud.controller.UserController
import org.tty.dailyset.dailyset_cloud.util.anyTextEmpty


/**
 * request bean class used for [UserController]
 */
class UserRegisterReq(
    val nickname: String? = null,
    val password: String? = null,
    val email: String? = null,
    val portraitId: String? = null,
) {
    fun verify(): Boolean =
        !anyTextEmpty(nickname, password, email)
}