/**
 * create at 2022/4/16
 * @author h1542462994
 */

package org.tty.dailyset.dailyset_cloud.intent

class UserRegisterIntent(
    val nickname: String,
    val password: String,
    val email: String,
    val portraitId: String,
): UserIntent