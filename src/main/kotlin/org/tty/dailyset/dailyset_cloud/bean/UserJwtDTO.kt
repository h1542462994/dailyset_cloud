package org.tty.dailyset.dailyset_cloud.bean

import java.util.*

data class UserJwtDTO(
    val uid: Int,
    val expiredAt: Date,
    val issuedAt: Date,
    val valid: Boolean,
    val deviceCode: String,
)