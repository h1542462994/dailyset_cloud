/**
 * create at 2022/4/15
 * author h1542462994
 *
 * request bean class UserLoginReq
 */

package org.tty.dailyset.dailyset_cloud.bean.req

import org.tty.dailyset.dailyset_cloud.bean.enums.PlatformCode
import org.tty.dailyset.dailyset_cloud.controller.UserController
import org.tty.dailyset.dailyset_cloud.util.anyTextEmpty
import org.tty.dailyset.dailyset_cloud.util.anyIntEmpty

/**
 * request class UserLoginReq
 * used for [UserController]
 */
class UserLoginReq(
    val uid: Int? = null,
    val password: String? = null,
    val deviceCode: String? = null,
    val deviceName: String? = null,
    val platformCode: Int? = null
) {
    fun verify(): Boolean {
        return !anyIntEmpty(uid, platformCode) && !anyTextEmpty(password, deviceName)
                && PlatformCode.values().any { it.code == platformCode }
    }
}