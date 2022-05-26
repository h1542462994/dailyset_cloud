package org.tty.dailyset.dailyset_cloud.service

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.tty.dailyset.dailyset_cloud.bean.ResponseCodes
import org.tty.dailyset.dailyset_cloud.bean.Responses
import org.tty.dailyset.dailyset_cloud.bean.converters.toRTicketInfoResp
import org.tty.dailyset.dailyset_cloud.bean.entity.UserTicketBind
import org.tty.dailyset.dailyset_cloud.bean.isActive
import org.tty.dailyset.dailyset_cloud.bean.resp.TicketInfoResp
import org.tty.dailyset.dailyset_cloud.component.IntentFactory
import org.tty.dailyset.dailyset_cloud.grpc.stub.GrpcClientStubs
import org.tty.dailyset.dailyset_cloud.intent.TicketBindIntent
import org.tty.dailyset.dailyset_cloud.intent.UserStateIntent
import org.tty.dailyset.dailyset_cloud.mapper.UserTicketBindMapper

@Component
class TicketService {

    @Autowired
    private lateinit var userService: UserService

    @Autowired
    private lateinit var intentFactory: IntentFactory

    @Autowired
    private lateinit var userTicketBindMapper: UserTicketBindMapper

    @Autowired
    private lateinit var grpcClientStubs: GrpcClientStubs

    private val logger = LoggerFactory.getLogger(TicketService::class.java)

    @Suppress("DuplicatedCode")
    suspend fun bind(intent: TicketBindIntent): Responses<Unit> {
        val userState = userService.internalState(
            intentFactory.createUserStateIntent(intent.token)
        )

        if (!userState.isActive()) {
            return Responses.tokenError()
        }

        requireNotNull(userState.user)
        val userTicketBindExisted = userTicketBindMapper.findByUid(userState.user.uid)

        // 当用户已绑定时
        if (userTicketBindExisted != null) {
            return Responses.ticketExist()
        }

        try {
            // 请求建立ticket
            val client = grpcClientStubs.getTicketClient()
            val response = client.bind {
                uid = intent.studentUid
                password = intent.password
            }

            return if (response.success) {
                val userTicketBind = UserTicketBind(userState.user.uid, ticketId = response.ticket.ticketId)
                userTicketBindMapper.add(userTicketBind)
                Responses.ok()
            } else {
                Responses.fail(message = "绑定失败")
            }
        } catch (e: Exception) {
            logger.error("on ticketBind", e)
            return Responses.unicError(message = "服务发生了未知异常 ${e.message}")
        }
    }

    suspend fun rebind(intent: TicketBindIntent): Responses<Unit> {
        val userState = userService.internalState(
            intentFactory.createUserStateIntent(intent.token)
        )

        if (!userState.isActive()) {
            return Responses.tokenError()
        }

        requireNotNull(userState.user)

        // if not bind
        val userTicketBindExisted = userTicketBindMapper.findByUid(userState.user.uid)
            ?: return Responses.ticketExist()


        try {
            // call unbind
            val resultUnbind = grpcClientStubs.getTicketClient().unbind {
                ticketId = userTicketBindExisted.ticketId
            }
            if (!resultUnbind.success) {
                return Responses.unicError(message = "在解除绑定中发生了错误")
            }
            // call bind
            val resultBind = grpcClientStubs.getTicketClient().bind {
                uid = intent.studentUid
                password = intent.password
            }
            if (!resultBind.success) {
                return Responses.unicError(message = "在绑定中发生了错误")
            }
            val userTicketBind = UserTicketBind(userState.user.uid, ticketId = resultBind.ticket.ticketId)
            userTicketBindMapper.update(userTicketBind)
            return Responses.ok()
        } catch (e: Exception) {
            logger.error("on rebind", e)
            return Responses.unicError(message = "服务发生了未知异常 ${e.message}")
        }


    }

    suspend fun unbind(intent: UserStateIntent): Responses<Unit> {
        val userState = userService.internalState(
            intentFactory.createUserStateIntent(intent.token)
        )

        if (!userState.isActive()) {
            return Responses.tokenError()
        }

        requireNotNull(userState.user)
        val userTicketBindExisted = userTicketBindMapper.findByUid(userState.user.uid)
            ?: return Responses.ticketNotExist()

        try {
            // call unbind
            val resultUnbind = grpcClientStubs.getTicketClient().unbind {
                ticketId = userTicketBindExisted.ticketId
            }
            if (!resultUnbind.success) {
                return Responses.fail(message = "在解除绑定中发生了错误")
            }
            userTicketBindMapper.remove(userTicketBindExisted.uid)
            return Responses.ok()
        } catch (e: Exception) {
            logger.error("on unbind", e)
            return Responses.unicError(message = "服务发生了未知异常 ${e.message}")
        }
    }

    suspend fun currentBindInfo(intent: UserStateIntent): Responses<TicketInfoResp> {
        val userState = userService.internalState(
            intentFactory.createUserStateIntent(intent.token)
        )

        if (!userState.isActive()) {
            return Responses.tokenError()
        }

        requireNotNull(userState.user)
        val userTicketBindExisted = userTicketBindMapper.findByUid(userState.user.uid)
            ?: return Responses.ticketNotExist()



        return try {
            val response = grpcClientStubs.getTicketClient().query {
                ticketId = userTicketBindExisted.ticketId
            }
            response.toRTicketInfoResp()
        } catch (e: Exception) {
            logger.error("on ticket query", e)
            Responses.unicError(message = "服务发生了未知异常 ${e.message}")
        }

    }

    suspend fun forceFetch(intent: UserStateIntent): Responses<Unit> {
        val userState = userService.internalState(
            intentFactory.createUserStateIntent(intent.token)
        )

        if (!userState.isActive()) {
            return Responses.tokenError()
        }

        requireNotNull(userState.user)

        // if not bind
        val userTicketBindExisted = userTicketBindMapper.findByUid(userState.user.uid)
            ?: return Responses.ticketNotExist()

        return try {
            val response = grpcClientStubs.getTicketClient().forceFetch {
                ticketId = userTicketBindExisted.ticketId
            }
            if (response.success) {
                Responses.ok()
            } else {
                Responses.fail(message = "强制刷新失败")
            }
        } catch (e: Exception) {
            logger.error("on ticket force fetch", e)
            Responses.unicError(message = "服务发生了未知异常 ${e.message}")
        }
    }

}