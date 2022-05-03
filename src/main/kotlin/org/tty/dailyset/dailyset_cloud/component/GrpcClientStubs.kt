package org.tty.dailyset.dailyset_cloud.component

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.tty.dailyset.dailyset_unic.grpc.TicketServiceCoroutineGrpc

@Component
class GrpcClientStubs {

    @Autowired
    private lateinit var grpcClientCreator: GrpcClientCreator

    private var ticketClient: TicketServiceCoroutineGrpc.TicketServiceCoroutineStub? = null

    fun getTicketClient(): TicketServiceCoroutineGrpc.TicketServiceCoroutineStub {
        if (ticketClient == null) {
            ticketClient = TicketServiceCoroutineGrpc.newStub(grpcClientCreator.getChannel())
        }
        return ticketClient!!
    }

}