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
        val userTicketBindExisted = userTicketBindMapper.findUserTicketBindByUid(userState.user.uid)

        // 当用户已绑定时
        if (userTicketBindExisted != null) {
            return Responses.fail(ResponseCodes.ticketExist, "ticket已绑定")
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
                userTicketBindMapper.addUserTicketBind(userTicketBind)
                Responses.ok()
            } else {
                Responses.fail()
            }
        } catch (e: Exception) {
            logger.error("on ticketBind", e)
            return Responses.fail(message = "服务发生了未知异常 ${e.message}")
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
        val userTicketBindExisted = userTicketBindMapper.findUserTicketBindByUid(userState.user.uid)
            ?: return Responses.fail(ResponseCodes.ticketNotExist)


        try {
            // call unbind
            val resultUnbind = grpcClientStubs.getTicketClient().unbind {
                ticketId = userTicketBindExisted.ticketId
            }
            if (!resultUnbind.success) {
                return Responses.fail(message = "在解除绑定中发生了错误")
            }
            // call bind
            val resultBind = grpcClientStubs.getTicketClient().bind {
                uid = intent.studentUid
                password = intent.password
            }
            if (!resultBind.success) {
                return Responses.fail(message = "在绑定中发生了错误")
            }
            val userTicketBind = UserTicketBind(userState.user.uid, ticketId = resultBind.ticket.ticketId)
            userTicketBindMapper.updateUserTicketBindByUid(userTicketBind)
            return Responses.ok()
        } catch (e: Exception) {
            logger.error("on rebind", e)
            return Responses.fail(message = "服务发生了未知异常 ${e.message}")
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
        val userTicketBindExisted = userTicketBindMapper.findUserTicketBindByUid(userState.user.uid)
            ?: return Responses.fail(ResponseCodes.ticketNotExist)

        try {
            // call unbind
            val resultUnbind = grpcClientStubs.getTicketClient().unbind {
                ticketId = userTicketBindExisted.ticketId
            }
            if (!resultUnbind.success) {
                return Responses.fail(message = "在解除绑定中发生了错误")
            }
            return Responses.ok()
        } catch (e: Exception) {
            logger.error("on unbind", e)
            return Responses.fail(message = "服务发生了未知异常 ${e.message}")
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
        val userTicketBindExisted = userTicketBindMapper.findUserTicketBindByUid(userState.user.uid)
            ?: return Responses.fail(ResponseCodes.ticketNotExist, "ticket不存在")



        return try {
            val response = grpcClientStubs.getTicketClient().query {
                ticketId = userTicketBindExisted.ticketId
            }
            response.toRTicketInfoResp()
        } catch (e: Exception) {
            logger.error("on ticket query", e)
            Responses.fail(message = "服务发生了未知异常 ${e.message}")
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
        val userTicketBindExisted = userTicketBindMapper.findUserTicketBindByUid(userState.user.uid)
            ?: return Responses.fail(ResponseCodes.ticketNotExist)

        return try {
            val response = grpcClientStubs.getTicketClient().forceFetch {
                ticketId = userTicketBindExisted.ticketId
            }
            if (response.success) {
                Responses.ok()
            } else {
                Responses.fail()
            }
        } catch (e: Exception) {
            logger.error("on ticket force fetch", e)
            Responses.fail(message = "服务发生了未知异常 ${e.message}")
        }
    }

}