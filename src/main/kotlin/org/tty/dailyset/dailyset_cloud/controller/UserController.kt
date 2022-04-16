package org.tty.dailyset.dailyset_cloud.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController
import org.tty.dailyset.dailyset_cloud.bean.Responses
import org.tty.dailyset.dailyset_cloud.bean.req.UserRegisterReq
import org.tty.dailyset.dailyset_cloud.bean.resp.UserRegisterResp
import org.tty.dailyset.dailyset_cloud.component.IntentFactory
import org.tty.dailyset.dailyset_cloud.service.UserService

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


}