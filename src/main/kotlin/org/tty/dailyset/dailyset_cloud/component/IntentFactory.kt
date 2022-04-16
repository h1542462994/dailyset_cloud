/**
 * create at 2022/4/16
 * @author h1542462994
 *
 * component: intentFactory
 */

package org.tty.dailyset.dailyset_cloud.component

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.tty.dailyset.dailyset_cloud.bean.enums.PlatformCode
import org.tty.dailyset.dailyset_cloud.bean.req.UserLoginReq
import org.tty.dailyset.dailyset_cloud.bean.req.UserRegisterReq
import org.tty.dailyset.dailyset_cloud.intent.UserLoginIntent
import org.tty.dailyset.dailyset_cloud.intent.UserRegisterIntent
import org.tty.dailyset.dailyset_cloud.intent.UserStateIntent

/**
 * intentFactory
 * create *Intent
 */
@Component
class IntentFactory {
    @Autowired
    private lateinit var environmentVars: EnvironmentVars

    /**
     * create [UserRegisterIntent]
     */
    fun createUserRegisterIntent(userRegisterReq: UserRegisterReq): UserRegisterIntent {
        return UserRegisterIntent(
            nickname = userRegisterReq.nickname!!,
            password = userRegisterReq.password!!,
            email = userRegisterReq.email!!,
            portraitId = userRegisterReq.portraitId ?: environmentVars.defaultPortraitFileName
        )
    }

    /**
     * create [UserLoginIntent]
     */
    fun createUserLoginIntent(userLoginReq: UserLoginReq): UserLoginIntent {
        return UserLoginIntent(
            uid = userLoginReq.uid!!,
            password = userLoginReq.password!!,
            deviceCode = userLoginReq.deviceCode,
            deviceName = userLoginReq.deviceName!!,
            platformCode = userLoginReq.platformCode?.let { PlatformCode.of(it) }!!,
        )
    }

    /**
     * create [UserStateIntent]
     */
    fun createUserStateIntent(token: String): UserStateIntent {
        return UserStateIntent(token)
    }

}