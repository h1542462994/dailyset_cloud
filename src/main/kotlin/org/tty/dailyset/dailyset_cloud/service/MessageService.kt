package org.tty.dailyset.dailyset_cloud.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.tty.dailyset.dailyset_cloud.bean.Responses
import org.tty.dailyset.dailyset_cloud.component.MessageHolder
import org.tty.dailyset.dailyset_cloud.intent.MessagePostIntent
import org.tty.dailyset.dailyset_cloud.mapper.UserTicketBindMapper

@Service
class MessageService {

    @Value("\${dailyset.env.message.secret}")
    private lateinit var messageSecret: String

    @Autowired
    private lateinit var messageHolder: MessageHolder

    @Autowired
    private lateinit var userTicketBindMapper: UserTicketBindMapper

    /**
     * post system messages to the users, identified by **user.uid**
     */
    suspend fun messagePostSystem(messagePostSystemIntent: MessagePostIntent): Responses<Unit> {
        if (messagePostSystemIntent.secret != messageSecret) {
            return Responses.secretError()
        }
        messageHolder.postMessage(messagePostSystemIntent.targets, messagePostSystemIntent.intent)
        return Responses.ok()
    }

    /**
     * post ticket messages to the users, identified by **ticket_id**
     */
    suspend fun messagePostTicket(messagePostSystemIntent: MessagePostIntent): Responses<Unit> {
        if (messagePostSystemIntent.secret != messageSecret) {
            return Responses.secretError()
        }
        val userTicketBinds = userTicketBindMapper.findAllByTicketId(messagePostSystemIntent.targets)
        if (userTicketBinds.isNotEmpty()) {
            val userUids = userTicketBinds.map { it.uid }.distinct()
            messageHolder.postMessage(userUids, messagePostSystemIntent.intent)
        }
        return Responses.ok()
    }

}