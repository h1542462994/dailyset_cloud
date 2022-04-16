package org.tty.dailyset.dailyset_cloud.controller

import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.tty.dailyset.dailyset_cloud.bean.Responses

@RestController
class IndexController {
    @RequestMapping("/")
    fun index(): Responses<String> {
        return Responses.ok(data = "hello ?dailyset_cloud?")
    }
}