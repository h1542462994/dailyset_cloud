package org.tty.dailyset.dailyset_cloud.grpc.stub

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.tty.dailyset.dailyset_cloud.grpc.stub.GrpcClientFactory
import org.tty.dailyset.dailyset_unic.grpc.TicketServiceCoroutineGrpc

@Component
class GrpcClientStubs {

    @Autowired
    private lateinit var grpcClientCreator: GrpcClientFactory

    fun getTicketClient(): TicketServiceCoroutineGrpc.TicketServiceCoroutineStub {
        return TicketServiceCoroutineGrpc.newStub(grpcClientCreator.getChannel())
    }

}