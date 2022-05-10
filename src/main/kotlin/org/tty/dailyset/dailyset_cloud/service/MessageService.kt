package org.tty.dailyset.dailyset_cloud.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.tty.dailyset.dailyset_cloud.bean.Responses
import org.tty.dailyset.dailyset_cloud.component.MessageHolder
import org.tty.dailyset.dailyset_cloud.intent.MessagePostSystemIntent

@Service
class MessageService {

    @Value("\${dailyset.env.message.secret}")
    private lateinit var messageSecret: String

    @Autowired
    private lateinit var messageHolder: MessageHolder

    suspend fun messagePostSystem(messagePostSystemIntent: MessagePostSystemIntent): Responses<Int> {
        if (messagePostSystemIntent.secret != messageSecret) {
            return Responses.secretError()
        }
        messageHolder.postMessage(messagePostSystemIntent.targets, messagePostSystemIntent.intent)
        return Responses.ok()
    }
}