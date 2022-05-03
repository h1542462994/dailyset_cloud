package org.tty.dailyset.dailyset_cloud.service.grpc

import net.devh.boot.grpc.server.service.GrpcService
import org.springframework.beans.factory.annotation.Autowired
import org.tty.dailyset.dailyset_cloud.bean.ResponseCodes
import org.tty.dailyset.dailyset_cloud.bean.entity.UserTicketBind
import org.tty.dailyset.dailyset_cloud.bean.isActive
import org.tty.dailyset.dailyset_cloud.component.GrpcClientStubs
import org.tty.dailyset.dailyset_cloud.component.IntentFactory
import org.tty.dailyset.dailyset_cloud.grpc.TicketProtoBuilders.TicketRequest
import org.tty.dailyset.dailyset_cloud.grpc.TicketProtoBuilders.TicketResponse
import org.tty.dailyset.dailyset_cloud.grpc.TicketRequest
import org.tty.dailyset.dailyset_cloud.grpc.TicketResponse
import org.tty.dailyset.dailyset_cloud.grpc.TicketServiceCoroutineGrpc
import org.tty.dailyset.dailyset_cloud.mapper.UserTicketBindMapper
import org.tty.dailyset.dailyset_cloud.service.UserService

import org.tty.dailyset.dailyset_unic.grpc.TicketProtoBuilders.TicketRequest as CTicketRequest

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

    override suspend fun bind(request: TicketRequest): TicketResponse {
        val userState = userService.internalState(
            intentFactory.createUserStateIntent(request.token.value)
        )
        if (userState.isActive()) {
            requireNotNull(userState.user)
            val userTicketBindExisted = userTicketBindMapper.findUserTicketBindByUid(userState.user.uid)

            // 当用户已绑定时
            if (userTicketBindExisted != null) {
                return TicketResponse {
                    code = ResponseCodes.ticketExist
                    message = "ticket已绑定"
                }
            }

            // 请求建立ticket
            val client = grpcClientStubs.getTicketClient()
            val response = client.bind {
                CTicketRequest {
                    uid = request.uid
                    password = request.password
                }
            }

            // 建立成功
            return if (response.success) {
                val userTicketBind = UserTicketBind(userState.user.uid, ticketId = response.ticket.ticketId)
                userTicketBindMapper.addUserTicketBind(userTicketBind)
                TicketResponse {
                    code = ResponseCodes.success
                    message = "请求成功"
                }
            } else {
                TicketResponse {
                    code = ResponseCodes.fail
                    message = "请求失败"
                }
            }

        } else {

            // 当用户未登录时，返回错误码.
            return TicketResponse {
                code = ResponseCodes.tokenError
                message = "token错误"
            }
        }
    }
}