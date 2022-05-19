/**
 * create at 2022/4/15
 * @author h1542462994
 *
 * service class UserService
 */

package org.tty.dailyset.dailyset_cloud.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
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
import org.tty.dailyset.dailyset_cloud.intent.*
import org.tty.dailyset.dailyset_cloud.mapper.UserActivityMapper
import org.tty.dailyset.dailyset_cloud.mapper.UserMapper
import org.tty.dailyset.dailyset_cloud.util.uuid
import java.time.LocalDateTime

@Service
class UserService {

//    @Autowired
//    private lateinit var sysEnvMapper: SysEnvMapper\

    @Autowired
    private lateinit var preferenceService: PreferenceService

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
        val nextUidGenerate = preferenceService.nextUidGenerate
        val encryptPassword = encryptProvider.encrypt(nextUidGenerate.toString(), intent.password)

        val result = userMapper.addUser(nextUidGenerate, intent.nickname, intent.email, encryptPassword, intent.portraitId)
        return if (result >= 0) {
            preferenceService.nextUidGenerate = nextUidGenerate + 1
            Responses.ok(message = "注册成功", data = UserRegisterResp(nextUidGenerate, intent.nickname, intent.email, intent.portraitId))
        } else {
            Responses.fail()
        }
    }

    /**
     * login user
     */
    fun login(intent: UserLoginIntent): ResponseEntity<Responses<UserLoginResp>>   {

        // first check whether the user exists.
        val user = userMapper.findUserByUid(intent.uid)
            ?: return ResponseEntity.ok(Responses.userNoExist())

        if (environmentVars.profile.isDev() && user.uid == environmentVars.beginUid) {
            if (intent.password != user.password) {
                return ResponseEntity.ok(Responses.passwordError())
            }
        } else {
            // then check whether the password is correct.
            val encryptPassword = encryptProvider.encrypt(intent.uid.toString(), intent.password)
            if (encryptPassword != user.password) {
                return ResponseEntity.ok(Responses.passwordError())
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
            userActivityMapper.updateByUidAndDeviceCode(userActivity)
        } else {
            userActivityMapper.addUserActivity(userActivity)
        }

        return if (resultCode >= 0) {
            val token = jwtToken.sign(user, userActivity)

            return ResponseEntity.status(HttpStatus.OK).header(
                "Authorization", "Bearer $token"
            ).body(
                Responses.ok(message = "登录成功", data = UserLoginResp(
                    uid = intent.uid,
                    nickname = user.nickname,
                    email = user.email,
                    portraitId = user.portraitId,
                    token = token,
                    deviceCode = userActivity.deviceCode,
                    deviceName = userActivity.deviceName,
                    platformCode = userActivity.platformCode,
                ))
            )
        } else {
            ResponseEntity.ok(Responses.fail())
        }
    }

    fun autoLogin(intent: UserAutoLoginIntent): ResponseEntity<Responses<UserStateResp>> {
        // verify the token
        val token = jwtToken.verify(intent.token)
            ?: return ResponseEntity.ok(Responses.tokenError())

        // get the user
        val user = userMapper.findUserByUid(token.uid)
            ?: return ResponseEntity.ok(Responses.userNoExist())

        // get the userActivity
        val userActivity = userActivityMapper.findByUidAndDeviceCode(token.uid, token.deviceCode)
            ?: return ResponseEntity.ok(Responses.deviceCodeError())

        val newToken = jwtToken.sign(user, userActivity)
        val newUserActivity = userActivity.copy(
            state = PlatformState.ALIVE.state,
            lastActive = LocalDateTime.now()
        )

        val resultCode: Int = userActivityMapper.updateByUidAndDeviceCode(newUserActivity)
        if (resultCode > 0) {
            return ResponseEntity.status(HttpStatus.OK)
                .header("Authorization", "Bearer $newToken")
                .body(Responses.ok(message = "自动登录成功", data = UserStateResp(
                    uid = user.uid,
                    nickname = user.nickname,
                    email = user.email,
                    portraitId = user.portraitId,
                    deviceCode = newUserActivity.deviceCode,
                    deviceName = newUserActivity.deviceName,
                    platformCode = newUserActivity.platformCode,
                    token = newToken,
                    state = newUserActivity.state,
                    lastActive = newUserActivity.lastActive
                )) )
        } else {
            return ResponseEntity.ok(Responses.fail())
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
            lastActive = userActivity.lastActive,
            token = userStateIntent.token
        )

        return Responses.ok(message = "获取用户状态成功", data = userStateResp)
    }

    fun logout(userLogoutIntent: UserLogoutIntent): Responses<Int> {
        val updateUserActivity = userLogoutIntent.userActivity.copy(
            state = PlatformState.LEAVE.state
        )
        val result = userActivityMapper.updateByUidAndDeviceCode(updateUserActivity)
        return if (result > 0) {
            Responses.ok()
        } else {
            Responses.fail()
        }
    }

    fun internalState(userStateIntent: UserStateIntent): UserState? {
        // verify the token
        val token = jwtToken.verify(userStateIntent.token)
            ?: return null

        // get the user
        val user = userMapper.findUserByUid(token.uid) ?: return UserState(null, null, null)

        // get the userActivity
        val userActivity = userActivityMapper.findByUidAndDeviceCode(token.uid, token.deviceCode)

        return UserState(user, userActivity, userStateIntent.token)
    }

}
