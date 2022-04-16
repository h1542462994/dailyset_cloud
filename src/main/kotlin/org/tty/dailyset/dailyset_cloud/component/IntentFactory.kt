package org.tty.dailyset.dailyset_cloud.component

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.tty.dailyset.dailyset_cloud.bean.req.UserRegisterReq
import org.tty.dailyset.dailyset_cloud.intent.UserRegisterIntent

@Component
class IntentFactory {
    @Autowired
    private lateinit var environmentVars: EnvironmentVars

    fun createUserRegisterIntent(userRegisterReq: UserRegisterReq): UserRegisterIntent {
        return UserRegisterIntent(
            nickname = userRegisterReq.nickname!!,
            password = userRegisterReq.password!!,
            email = userRegisterReq.email!!,
            portraitId = userRegisterReq.portraitId ?: environmentVars.defaultPortraitFileName
        )
    }
}