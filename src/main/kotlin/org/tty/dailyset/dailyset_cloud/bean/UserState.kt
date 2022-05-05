/**
 * create at 2022/4/16
 * @author h1542462994
 */

@file:Suppress("OPT_IN_IS_NOT_ENABLED")

package org.tty.dailyset.dailyset_cloud.bean

import org.tty.dailyset.dailyset_cloud.bean.entity.User
import org.tty.dailyset.dailyset_cloud.bean.entity.UserActivity
import org.tty.dailyset.dailyset_cloud.bean.resp.UserStateResp
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

data class UserState(
    val user: User?,
    val userActivity: UserActivity?,
    val token: String?,
)

@OptIn(ExperimentalContracts::class)
fun UserState?.isActive(): Boolean {
    contract {
        returns(true) implies (this@isActive != null )
    }

    return this != null && user != null && userActivity != null
}

fun UserState?.castToUserStateResp(): UserStateResp {
    val user = this!!.user!!
    val userActivity = this.userActivity!!

    return UserStateResp(
        uid = user.uid,
        nickname = user.nickname,
        email = user.email,
        portraitId = user.portraitId,
        deviceCode = userActivity.deviceCode,
        deviceName = userActivity.deviceName,
        platformCode = userActivity.platformCode,
        state = userActivity.state,
        lastActive = userActivity.lastActive,
        token = this.token!!
    )
}
