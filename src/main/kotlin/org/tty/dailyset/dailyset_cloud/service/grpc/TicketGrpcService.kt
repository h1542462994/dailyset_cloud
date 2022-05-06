package org.tty.dailyset.dailyset_cloud.service.grpc

import net.devh.boot.grpc.server.service.GrpcService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.tty.dailyset.dailyset_cloud.bean.ResponseCodes
import org.tty.dailyset.dailyset_cloud.bean.converters.toCurrentBindInfoResponse
import org.tty.dailyset.dailyset_cloud.bean.entity.UserTicketBind
import org.tty.dailyset.dailyset_cloud.bean.isActive
import org.tty.dailyset.dailyset_cloud.component.GrpcBeanFactory
import org.tty.dailyset.dailyset_cloud.component.GrpcClientStubs
import org.tty.dailyset.dailyset_cloud.component.IntentFactory
import org.tty.dailyset.dailyset_cloud.grpc.*
import org.tty.dailyset.dailyset_cloud.grpc.TicketProtoBuilders.CurrentBindInfoResponse
import org.tty.dailyset.dailyset_cloud.grpc.TicketProtoBuilders.TicketBindResponse
import org.tty.dailyset.dailyset_cloud.mapper.UserTicketBindMapper
import org.tty.dailyset.dailyset_cloud.service.UserService

@GrpcService
class TicketGrpcService: TicketServiceCoroutineGrpc.TicketServiceImplBase() {

    @Autowired
    private lateinit var grpcClientStubs: GrpcClientStubs

    @Autowired
    private lateinit var userService: UserService

    @Autowired
    private lateinit var intentFactory: IntentFactory

    @Autowired
    private lateinit var userTicketBindMapper: UserTicketBindMapper

    @Autowired
    private lateinit var grpcBeanFactory: GrpcBeanFactory

    private val logger = LoggerFactory.getLogger(TicketServiceGrpc::class.java)

    /**
     * 绑定ticket
     */
    override suspend fun bind(request: TicketBindRequest): TicketBindResponse {
        val userState = userService.internalState(
            intentFactory.createUserStateIntent(request.token.value)
        )
        // 当用户未登录时，返回错误码.
        if (!userState.isActive()) {

            return TicketBindResponse {
                code = ResponseCodes.tokenError
                message = "token错误"
            }
        }

        requireNotNull(userState.user)
        val userTicketBindExisted = userTicketBindMapper.findUserTicketBindByUid(userState.user.uid)

        // 当用户已绑定时
        if (userTicketBindExisted != null) {
            return TicketBindResponse {
                code = ResponseCodes.ticketExist
                message = "ticket已绑定"
            }
        }

        // 请求建立ticket
        val client = grpcClientStubs.getTicketClient()
        val response = client.bind {
            uid = request.uid
            password = request.password
        }

        // 建立成功
        return if (response.success) {
            val userTicketBind = UserTicketBind(userState.user.uid, ticketId = response.ticket.ticketId)
            userTicketBindMapper.addUserTicketBind(userTicketBind)
            TicketBindResponse {
                code = ResponseCodes.success
                message = "请求成功"
            }
        } else {
            TicketBindResponse {
                code = ResponseCodes.fail
                message = "请求失败"
            }
        }
    }

    /**
     * 获取当前绑定信息
     */
    override suspend fun currentBindInfo(request: CurrentBindInfoRequest): CurrentBindInfoResponse {
        val userState = userService.internalState(
            intentFactory.createUserStateIntent(request.token.value)
        )

        // 当用户未登录时，返回错误码.
        if (!userState.isActive()) {
            return CurrentBindInfoResponse {
                code = ResponseCodes.tokenError
                message = "token错误"
                bindInfo = grpcBeanFactory.emptyBindInfo()
            }
        }

        // 当用户未绑定时
        requireNotNull(userState.user)
        val userTicketBindExisted = userTicketBindMapper.findUserTicketBindByUid(userState.user.uid)
            ?: return CurrentBindInfoResponse {
                code = ResponseCodes.ticketNotExist
                message = "ticket不存在"
                bindInfo = grpcBeanFactory.emptyBindInfo()
            }

        // 请求查询信息
        val response = grpcClientStubs.getTicketClient().query {
            ticketId = userTicketBindExisted.ticketId
        }

        return response.toCurrentBindInfoResponse()

    }
}