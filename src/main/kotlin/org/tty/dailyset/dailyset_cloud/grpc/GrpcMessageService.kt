package org.tty.dailyset.dailyset_cloud.grpc

import io.grpc.StatusRuntimeException
import kotlinx.coroutines.awaitCancellation
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.flow.collect
import net.devh.boot.grpc.server.service.GrpcService
import org.springframework.beans.factory.annotation.Autowired
import org.tty.dailyset.dailyset_cloud.bean.isActive
import org.tty.dailyset.dailyset_cloud.component.MessageHolder
import org.tty.dailyset.dailyset_cloud.grpc.MessageProtoBuilders.MessageBundle
import org.tty.dailyset.dailyset_cloud.intent.UserStateIntent
import org.tty.dailyset.dailyset_cloud.service.UserService

@GrpcService
class GrpcMessageService: MessageServiceCoroutineGrpc.MessageServiceImplBase() {
    @Autowired
    private lateinit var messageHolder: MessageHolder

    @Autowired
    private lateinit var userService: UserService

    override suspend fun connect(request: MessageBindRequest, responseChannel: SendChannel<MessageBundle>) {
        val token = request.token.value
        val userState = userService.internalState(UserStateIntent(token))

        if (userState.isActive()) {
            requireNotNull(userState.user)
            val messageChannel = messageHolder.connectMessageFlow(userState.user.uid.toString())
            try {
                messageChannel.collect {
                    responseChannel.send(MessageBundle {
                        topic = it.topic
                        referer = it.referer
                        code = it.code
                        content = it.content
                    })
                }
            } catch (e: Exception) {
                throw e
            } finally {
                messageHolder.disconnectMessageFlow(userState.user.uid.toString())
            }
        } else {
            responseChannel.close()
        }

    }

}