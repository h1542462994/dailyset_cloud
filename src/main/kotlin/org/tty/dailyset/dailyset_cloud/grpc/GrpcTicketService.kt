package org.tty.dailyset.dailyset_cloud.grpc

import net.devh.boot.grpc.server.service.GrpcService
import org.springframework.beans.factory.annotation.Autowired
import org.tty.dailyset.dailyset_cloud.bean.converters.toCurrentTicketInfoResp
import org.tty.dailyset.dailyset_cloud.bean.converters.toSimpleResponse
import org.tty.dailyset.dailyset_cloud.component.IntentFactory
import org.tty.dailyset.dailyset_cloud.service.TicketService

@GrpcService
class GrpcTicketService: TicketServiceCoroutineGrpc.TicketServiceImplBase() {
    @Autowired
    private lateinit var intentFactory: IntentFactory

    @Autowired
    private lateinit var ticketService: TicketService

    /**
     * 绑定ticket
     */
    override suspend fun bind(request: TicketBindRequest): SimpleResponse {
        val intent = intentFactory.createTicketBindIntent(request)
        val response = ticketService.bind(intent)
        return response.toSimpleResponse()
    }

    /**
     * 获取当前绑定信息
     */
    override suspend fun currentBindInfo(request: SimpleRequest): CurrentBindInfoResponse {
        val intent = intentFactory.createUserStateIntent(request.token.value)
        val response = ticketService.currentBindInfo(intent)
        return response.toCurrentTicketInfoResp()
    }

    /**
     * 重新绑定ticket
     */
    override suspend fun rebind(request: TicketBindRequest): SimpleResponse {
        val intent = intentFactory.createTicketBindIntent(request)
        val response = ticketService.rebind(intent)
        return response.toSimpleResponse()
    }

    override suspend fun unbind(request: SimpleRequest): SimpleResponse {
        val intent = intentFactory.createUserStateIntent(request.token.value)
        val response = ticketService.unbind(intent)
        return response.toSimpleResponse()
    }

    override suspend fun forceFetch(request: SimpleRequest): SimpleResponse {
        val intent = intentFactory.createUserStateIntent(request.token.value)
        val response = ticketService.forceFetch(intent)
        return response.toSimpleResponse()
    }
}