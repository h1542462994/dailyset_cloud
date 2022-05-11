/**
 * create at 2022/4/16
 * @author h1542462994
 *
 * component: intentFactory
 */

package org.tty.dailyset.dailyset_cloud.component

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.tty.dailyset.dailyset_cloud.bean.entity.DailySet
import org.tty.dailyset.dailyset_cloud.bean.enums.PlatformCode
import org.tty.dailyset.dailyset_cloud.bean.req.*
import org.tty.dailyset.dailyset_cloud.intent.*

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

    fun createUserAutoLoginIntent(userAutoLoginReq: UserAutoLoginReq): UserAutoLoginIntent {
        return UserAutoLoginIntent(userAutoLoginReq.token!!)
    }

    fun createDailySetUpdateIntent(userUid: Int, dailysetUpdateReq: DailySetUpdateReq): DailySetUpdateIntent {
        return DailySetUpdateIntent(
            userUid = userUid,
            dailySet = DailySet(
                uid = dailysetUpdateReq.uid!!,
                type = dailysetUpdateReq.type!!,
                sourceVersion = dailysetUpdateReq.sourceVersion!!,
                matteVersion = dailysetUpdateReq.matteVersion!!,
                metaVersion = dailysetUpdateReq.metaVersion!!,
            )
        )
    }

    fun createMessagePostSystemIntent(messagePostSystemReq: MessagePostReq): MessagePostIntent {
        return MessagePostIntent(
            secret = messagePostSystemReq.secret!!,
            targets = messagePostSystemReq.targets,
            intent = messagePostSystemReq.intent!!
        )
    }

}