package org.tty.dailyset.dailyset_cloud.grpc.stub

import io.grpc.ManagedChannel
import io.grpc.ManagedChannelBuilder
import io.grpc.netty.GrpcSslContexts
import io.grpc.netty.NettyChannelBuilder
import io.netty.handler.ssl.SslContext
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.io.InputStream
import java.net.InetAddress

@Component
class GrpcClientFactory {
    @Value("\${dailyset.env.client.grpc.port}")
    private var grpcPort: Int = 0
    @Value("\${dailyset.env.client.address}")
    private lateinit var grpcAddress: String
    @Value("\${dailyset.env.client.security.enabled}")
    private var grpcSecurityEnabled = false
    @Value("\${dailyset.env.client.certificate-authority}")
    private lateinit var grpcClientCA: InputStream
    @Value("\${dailyset.env.client.certificate-chain}")
    private lateinit var grpcClientCrt: InputStream
    @Value("\${dailyset.env.client.private-key}")
    private lateinit var grpcGrpcKey: InputStream
    @Value("\${dailyset.env.client.usedocker}")
    private var useDocker = false


    private var channel: ManagedChannel? = null
    private var sslContext: SslContext? = null

    private val logger = LoggerFactory.getLogger(GrpcClientFactory::class.java)

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
            var realAddress = grpcAddress
            if (useDocker) {
                val inetAddress = InetAddress.getByName(grpcAddress)
                realAddress = inetAddress.hostAddress
            }

            logger.debug("real unic grpc address is $realAddress")

            channel = if (!grpcSecurityEnabled) {
                ManagedChannelBuilder.forAddress(realAddress, grpcPort)
                    .usePlaintext()
                    .build()
            } else {
                NettyChannelBuilder.forAddress(realAddress, grpcPort)
                    .sslContext(getSslContext())
                    .build()
            }
        }
        return channel!!
    }
}