package org.tty.dailyset.dailyset_cloud.intent

import org.tty.dailyset.dailyset_cloud.intent.UserIntent

class UserRegisterIntent(
    val nickname: String,
    val password: String,
    val email: String,
    val portraitId: String,
): UserIntent {

}