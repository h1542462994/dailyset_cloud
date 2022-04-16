package org.tty.dailyset.dailyset_cloud.bean.resp

class UserLoginResp(
    val uid: Int,
    val nickname: String,
    val email: String,
    val portraitId: String,
    val token: String,
    val deviceCode: String,
    val deviceName: String,
    val platformCode: Int,
)