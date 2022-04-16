package org.tty.dailyset.dailyset_cloud.bean.entity

data class User(
    val uid: Int,
    val nickname: String,
    val password: String,
    val email: String,
    val portraitId: String
)