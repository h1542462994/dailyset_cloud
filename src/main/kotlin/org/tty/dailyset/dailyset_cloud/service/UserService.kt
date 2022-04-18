/**
 * create at 2022/4/15
 * @author h1542462994
 *
 * service class UserService
 */

package org.tty.dailyset.dailyset_cloud.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.tty.dailyset.dailyset_cloud.bean.Responses
import org.tty.dailyset.dailyset_cloud.bean.UserState
import org.tty.dailyset.dailyset_cloud.bean.entity.UserActivity
import org.tty.dailyset.dailyset_cloud.bean.enums.PlatformState
import org.tty.dailyset.dailyset_cloud.bean.resp.UserLoginResp
import org.tty.dailyset.dailyset_cloud.bean.resp.UserRegisterResp
import org.tty.dailyset.dailyset_cloud.bean.resp.UserStateResp
import org.tty.dailyset.dailyset_cloud.component.EncryptProvider
import org.tty.dailyset.dailyset_cloud.component.EnvironmentVars
import org.tty.dailyset.dailyset_cloud.component.JwtToken
import org.tty.dailyset.dailyset_cloud.intent.UserLoginIntent
import org.tty.dailyset.dailyset_cloud.intent.UserRegisterIntent
import org.tty.dailyset.dailyset_cloud.intent.UserStateIntent
import org.tty.dailyset.dailyset_cloud.mapper.SysEnvMapper
import org.tty.dailyset.dailyset_cloud.mapper.UserActivityMapper
import org.tty.dailyset.dailyset_cloud.mapper.UserMapper
import org.tty.dailyset.dailyset_cloud.util.uuid
import java.time.LocalDateTime

@Service
class UserService {

    @Autowired
    private lateinit var sysEnvMapper: SysEnvMapper

    @Autowired
    private lateinit var userMapper: UserMapper

    @Autowired
    private lateinit var userActivityMapper: UserActivityMapper

    @Autowired
    private lateinit var encryptProvider: EncryptProvider

    @Autowired
    private lateinit var jwtToken: JwtToken

    @Autowired
    private lateinit var environmentVars: EnvironmentVars


    /**
     * register user
     */
    fun register(intent: UserRegisterIntent): Responses<UserRegisterResp> {
        val sysEnv = sysEnvMapper.find()
        val nextUidGenerate = sysEnv.nextUidGenerate
        val encryptPassword = encryptProvider.encrypt(nextUidGenerate.toString(), intent.password)

        val result = userMapper.addUser(nextUidGenerate, intent.nickname, intent.email, encryptPassword, intent.portraitId)
        return if (result >= 0) {
            sysEnvMapper.updateNextUidGenerate(nextUidGenerate + 1)
            Responses.ok(message = "注册成功", data = UserRegisterResp(nextUidGenerate, intent.nickname, intent.email, intent.portraitId))
        } else {
            Responses.fail()
        }
    }

    /**
     * login user
     */
    fun login(intent: UserLoginIntent): Responses<UserLoginResp> {

        // first check whether the user exists.
        val user = userMapper.findUserByUid(intent.uid)
            ?: return Responses.userNoExist()

        if (environmentVars.profile.isDev() && user.uid == environmentVars.beginUid) {
            if (intent.password != user.password) {
                return Responses.passwordError()
            }
        } else {
            // then check whether the password is correct.
            val encryptPassword = encryptProvider.encrypt(intent.uid.toString(), intent.password)
            if (encryptPassword != user.password) {
                return Responses.passwordError()
            }
        }

        // whether you need to create a new userActivity.
        var needCreate = false

        // assign a new deviceCode if not exists.
        val deviceCode = if (intent.deviceCode == null) {
            needCreate = true
            uuid()
        } else {
            intent.deviceCode
        }

        /**
         * if not exists, create a new userActivity.
         */
        if (!needCreate) {
            val userActivity: UserActivity? = userActivityMapper.findByUidAndDeviceCode(intent.uid, deviceCode)
            if (userActivity == null) {
                needCreate = true
            }
        }

        /**
         * updated userActivity.
         */
        val userActivity = UserActivity(
            uid = intent.uid,
            deviceCode = deviceCode,
            deviceName = intent.deviceName,
            platformCode = intent.platformCode.code,
            state = PlatformState.ALIVE.state,
            lastActive = LocalDateTime.now()
        )

        /**
         * create a new userActivity if needed. else update the userActivity.
         */
        val resultCode: Int = if (!needCreate) {
            userActivityMapper.updateByUidAndDeviceCode(userActivity.uid, userActivity.deviceCode, userActivity.deviceName, userActivity.platformCode, userActivity.state, userActivity.lastActive)
        } else {
            userActivityMapper.addUserActivity(userActivity.uid, userActivity.deviceCode, userActivity.deviceName, userActivity.platformCode, userActivity.state, userActivity.lastActive)
        }

        return if (resultCode >= 0) {
            val token = jwtToken.sign(user, userActivity)

            return Responses.ok(message = "登录成功", data = UserLoginResp(
                    uid = intent.uid,
                    nickname = user.nickname,
                    email = user.email,
                    portraitId = user.portraitId,
                    token = token,
                    deviceCode = userActivity.deviceCode,
                    deviceName = userActivity.deviceName,
                    platformCode = userActivity.platformCode,
                ))
        } else {
             Responses.fail()
        }
    }

    /**
     * state of user
     */
    fun state(userStateIntent: UserStateIntent): Responses<UserStateResp> {
        // verify the token
        val token = jwtToken.verify(userStateIntent.token)
            ?: return Responses.tokenError()

        // get the user
        val user = userMapper.findUserByUid(token.uid)
            ?: return Responses.userNoExist()

        // get the userActivity
        val userActivity = userActivityMapper.findByUidAndDeviceCode(token.uid, token.deviceCode)
            ?: return Responses.deviceCodeError()

        val userStateResp = UserStateResp(
            uid = user.uid,
            nickname = user.nickname,
            email = user.email,
            portraitId = user.portraitId,
            deviceCode = userActivity.deviceCode,
            deviceName = userActivity.deviceName,
            platformCode = userActivity.platformCode,
            state = userActivity.state,
            lastActive = userActivity.lastActive
        )

        return Responses.ok(message = "获取用户状态成功", data = userStateResp)
    }

    fun internalState(userStateIntent: UserStateIntent): UserState? {
        // verify the token
        val token = jwtToken.verify(userStateIntent.token)
            ?: return null

        // get the user
        val user = userMapper.findUserByUid(token.uid) ?: return UserState(null, null)

        // get the userActivity
        val userActivity = userActivityMapper.findByUidAndDeviceCode(token.uid, token.deviceCode)

        return UserState(user, userActivity)
    }

}
