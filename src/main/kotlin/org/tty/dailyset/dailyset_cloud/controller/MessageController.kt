package org.tty.dailyset.dailyset_cloud.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import org.tty.dailyset.dailyset_cloud.auth.Anonymous
import org.tty.dailyset.dailyset_cloud.bean.Responses
import org.tty.dailyset.dailyset_cloud.bean.req.MessagePostReq
import org.tty.dailyset.dailyset_cloud.component.IntentFactory
import org.tty.dailyset.dailyset_cloud.service.MessageService

@RestController
class MessageController {

    @Autowired
    private lateinit var messageService: MessageService

    @Autowired
    private lateinit var intentFactory: IntentFactory

    @Anonymous
    @PostMapping("/message/post/system")
    suspend fun messagePostSystem(@RequestBody req: MessagePostReq): Responses<Unit> {
        if (!req.verify()) {
            return Responses.argError()
        }

        val intent = intentFactory.createMessagePostSystemIntent(req)
        return messageService.messagePostSystem(intent)
    }

    @Anonymous
    @PostMapping("/message/post/ticket")
    suspend fun messagePostTicket(@RequestBody req: MessagePostReq): Responses<Unit> {
        if (!req.verify()) {
            return Responses.argError()
        }

        val intent = intentFactory.createMessagePostSystemIntent(req)
        return messageService.messagePostTicket(intent)
    }


}