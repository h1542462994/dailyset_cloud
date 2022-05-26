/**
 * create at 2022/4/16
 * @author h1542462994
 */

package org.tty.dailyset.dailyset_cloud.bean.resp

class UserLoginResp(
    val uid: String,
    val nickname: String,
    val email: String,
    val portraitId: String,
    val token: String,
    val deviceCode: String,
    val deviceName: String,
    val platformCode: Int,
)