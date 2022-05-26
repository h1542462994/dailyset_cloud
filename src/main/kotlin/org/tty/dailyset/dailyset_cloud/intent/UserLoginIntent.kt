/**
 * create at 2022/4/16
 * @author h1542462994
 *
 * intent class UserLoginIntent
 */

package org.tty.dailyset.dailyset_cloud.intent

import org.tty.dailyset.dailyset_cloud.bean.enums.PlatformCode
import org.tty.dailyset.dailyset_cloud.service.UserService

/**
 * user login intent class
 * user for [UserService]
 */
class UserLoginIntent(
    val uid: String,
    val password: String,
    val deviceCode: String?,
    val deviceName: String,
    val platformCode: PlatformCode
): UserIntent