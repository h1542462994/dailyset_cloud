package org.tty.dailyset.dailyset_cloud.service.grpc

import net.devh.boot.grpc.server.service.GrpcService
import org.tty.dailyset.dailyset_cloud.grpc.HelloCoroutineGrpc
import org.tty.dailyset.dailyset_cloud.grpc.HelloProtoBuilders.HelloReply
import org.tty.dailyset.dailyset_cloud.grpc.HelloReply
import org.tty.dailyset.dailyset_cloud.grpc.HelloRequest

@GrpcService
class HelloService: HelloCoroutineGrpc.HelloImplBase() {
    override suspend fun sayHello(request: HelloRequest): HelloReply {
        return HelloReply {
            message = "Hello ${request.name}"
        }
    }
}