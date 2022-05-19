package org.tty.dailyset.dailyset_cloud.intent

import org.tty.dailyset.dailyset_cloud.bean.entity.User
import org.tty.dailyset.dailyset_cloud.bean.entity.UserActivity

class UserLogoutIntent(
    val user: User,
    val userActivity: UserActivity,
    val token: String
): UserIntent