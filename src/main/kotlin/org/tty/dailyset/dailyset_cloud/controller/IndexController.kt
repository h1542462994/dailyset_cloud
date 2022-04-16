/**
 * create at 2022/4/16
 * author h1542462994
 *
 * controller class IndexController
 */

package org.tty.dailyset.dailyset_cloud.controller

import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.tty.dailyset.dailyset_cloud.bean.Responses
import org.tty.dailyset.dailyset_cloud.bean.UserState
import org.tty.dailyset.dailyset_cloud.bean.isActive

/**
 * controller class index
 */
@RestController
class IndexController {
    /**
     * to check the health of the cloud service.
     */
    @RequestMapping("/")
    fun index(): Responses<String> {
        return Responses.ok(data = "hello ?dailyset_cloud?")
    }

    @RequestMapping("/me")
    fun me(state: UserState?): Responses<String> {
        return if (!state.isActive()) {
            Responses.ok(data = "no login")
        } else {
            Responses.ok(data = "hello ${state.user?.nickname}")
        }
    }
}