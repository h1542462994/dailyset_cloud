package org.tty.dailyset.dailyset_cloud.component

import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.tty.dailyset.dailyset_cloud.bean.MessageIntent

@Component
class MessageHolder {
    private val messageFlowSaver: HashMap<String, SharedFlow<MessageIntent>> = hashMapOf()
    private val messageFlowCounts: HashMap<String, Int> = hashMapOf()

    private val logger = LoggerFactory.getLogger(MessageHolder::class.java)

    /**
     * used for connect the message flow.
     */
    fun connectMessageFlow(uid: String): SharedFlow<MessageIntent> {
        synchronized(messageFlowSaver) {
            val existed = messageFlowSaver[uid]
            if (existed == null) {
                messageFlowSaver[uid] = MutableSharedFlow(replay = 0, extraBufferCapacity = 10, onBufferOverflow = BufferOverflow.DROP_OLDEST)
                messageFlowCounts[uid] = 0
            }

            messageFlowCounts[uid] = messageFlowCounts[uid]!! + 1
            logger.info("user $uid connected message service, now count: ${messageFlowCounts[uid]}")
            return messageFlowSaver[uid]!!
        }
    }

    /**
     * used for disconnect the message flow. it will happen if device disconnect the service.
     */
    fun disconnectMessageFlow(uid: String) {
        synchronized(messageFlowSaver) {
            messageFlowSaver[uid] ?: return
            messageFlowCounts[uid] = messageFlowCounts[uid]!! - 1
            if (messageFlowCounts[uid] == 0) {
                messageFlowSaver.remove(uid)
            }
            logger.info("user $uid disconnected message service, now count: ${messageFlowCounts[uid]}")
        }
    }

    suspend fun postMessage(uids: List<String>, messageIntent: MessageIntent) {
        val availableMessageFlows = messageFlowSaver.filter {
            it.key in uids
        }.map { it.value }

        for (messageFlow in availableMessageFlows) {
            (messageFlow as MutableSharedFlow<MessageIntent>).tryEmit(messageIntent)
        }
    }
}