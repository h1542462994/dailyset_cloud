@file:Suppress("OPT_IN_IS_NOT_ENABLED")

package org.tty.dailyset.dailyset_cloud.bean

import org.tty.dailyset.dailyset_cloud.bean.entity.User
import org.tty.dailyset.dailyset_cloud.bean.entity.UserActivity
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

data class UserState(
    val user: User?,
    val userActivity: UserActivity?
)

@OptIn(ExperimentalContracts::class)
fun UserState?.isActive(): Boolean {
    contract {
        returns(true) implies (this@isActive != null )
    }

    return this != null && user != null && userActivity != null
}