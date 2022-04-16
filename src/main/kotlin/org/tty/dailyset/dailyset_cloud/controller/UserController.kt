/**
 * create at 2022/4/15
 * @author h1542462994
 *
 * controller class UserController
 */

package org.tty.dailyset.dailyset_cloud.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RestController
import org.tty.dailyset.dailyset_cloud.bean.Responses
import org.tty.dailyset.dailyset_cloud.bean.req.UserLoginReq
import org.tty.dailyset.dailyset_cloud.bean.req.UserRegisterReq
import org.tty.dailyset.dailyset_cloud.bean.resp.UserLoginResp
import org.tty.dailyset.dailyset_cloud.bean.resp.UserRegisterResp
import org.tty.dailyset.dailyset_cloud.bean.resp.UserStateResp
import org.tty.dailyset.dailyset_cloud.component.IntentFactory
import org.tty.dailyset.dailyset_cloud.service.UserService
import org.tty.dailyset.dailyset_cloud.util.getToken

/**
 * controller class userController
 * @see UserService
 */
@RestController
class UserController {

    @Autowired
    private lateinit var userService: UserService

    @Autowired
    private lateinit var intentFactory: IntentFactory

    @PostMapping("/user/register")
    fun register(userRegisterReq: UserRegisterReq): Responses<UserRegisterResp> {
        if (!userRegisterReq.verify()) {
            return Responses.argError()
        }

        val intent = intentFactory.createUserRegisterIntent(userRegisterReq)

        return userService.register(intent)
    }

    @PostMapping("/user/login")
    fun login(userLoginReq: UserLoginReq): Responses<UserLoginResp> {
        if (!userLoginReq.verify()) {
            return Responses.argError()
        }

        val intent = intentFactory.createUserLoginIntent(userLoginReq)

        return userService.login(intent)
    }

    @PostMapping("/user/state")
    fun state(@RequestHeader("Authorization") authorization: String): Responses<UserStateResp> {
        val token = getToken(authorization) ?: return Responses.tokenError()
        val intent = intentFactory.createUserStateIntent(token)

        return userService.state(intent)
    }

}