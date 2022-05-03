package org.tty.dailyset.dailyset_cloud.component

import io.grpc.ManagedChannel
import io.grpc.ManagedChannelBuilder
import io.grpc.netty.GrpcSslContexts
import io.grpc.netty.NettyChannelBuilder
import io.netty.handler.ssl.SslContext
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.io.File

@Component
class GrpcClientCreator {
    @Value("\${dailyset.env.grpc.unic.port}")
    private var grpcPort: Int = 0
    @Value("\${dailyset.env.grpc.unic.address}")
    private lateinit var grpcAddress: String
    @Value("\${grpc.server.security.enabled}")
    private var grpcSecurityEnabled = false
    @Value("\${dailyset.env.client.certificate-authority}")
    private lateinit var grpcClientCA: File
    @Value("\${dailyset.env.client.certificate-chain}")
    private lateinit var grpcClientCrt: File
    @Value("\${dailyset.env.client.private-key}")
    private lateinit var grpcGrpcKey: File

    private var channel: ManagedChannel? = null
    private var sslContext: SslContext? = null

    fun getSslContext(): SslContext {
        if (sslContext == null) {
            val builder = GrpcSslContexts.forClient()
            builder.trustManager(grpcClientCA)
            builder.keyManager(grpcClientCrt, grpcGrpcKey)
            sslContext = builder.build()
        }
        return sslContext!!
    }

    fun getChannel(): ManagedChannel {
        if (channel == null) {
            channel = if (!grpcSecurityEnabled) {
                ManagedChannelBuilder.forAddress(grpcAddress, grpcPort)
                    .usePlaintext()
                    .build()
            } else {
               NettyChannelBuilder.forAddress(grpcAddress, grpcPort)
                    .sslContext(getSslContext())
                    .build()
            }
        }
        return channel!!
    }
}