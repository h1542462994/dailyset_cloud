# 伪代码

```kotlin
data class Set(
    val uid: String,
    val version: Int
)

enum class SyncAction {
    Insert,
    Update,
    Remove;
}

data class TemporalLink(
    val setUid: String,
    val resourceUid: String,
    val action: SyncAction,
    val lastTick: LocalDateTime
)

data class Resource(
    val resourceUid: String,
    //... values
)

data class TemporalLinkResource(
    val link: TemporalLink,
    val resource: Resource
)

fun submitUpdate(set: Set) {
    // --- start client code ---
    // 找到需要写入的数据
    val temporalLs: List<TemporalLinkResource> = client.collect()
    val clientNow: LocalDateTime = now()
    server.callSubmit(set, temporalLs, clientNow)
    // --- end client code ---
    
    // --- start server code ---
    val serverNow: LocalDateTime = now()
    // 将所有temporalLs的lastTick改为lastTick + (serverNow - clientNow)，代码略
    val existLinks: List<Link> = findExistLinks(temporalLs.map { it.link })
    // 找出需要更新的linkResource
    var needUpdates: List<LinkResource> = calculateNeedSubmit(existLinks, temporalLs)
    // 更新版本号为当前的版本号+1
    needUpdates = updateNeedUpdatesVersions(needUpdates, set.version + 1)
    linkMapper.updateAll(needUpdates.map { it.link })
    resourceMapper.updateAll(needUpdates.map { it.resource })
    server.returns()
    // --- end server code ---
    
    // --- start client code ---
    // 移除临时的资源
    removeTemporalLs()
    // ...调用客户端请求更新的流程
    // --- end client code ---
}

```


```kotlin
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
```