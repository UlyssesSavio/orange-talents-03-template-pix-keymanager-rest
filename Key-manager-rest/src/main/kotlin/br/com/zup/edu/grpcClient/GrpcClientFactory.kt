package br.com.zup.edu.grpcClient

import br.com.zup.edu.KeyManagerServiceGrpc
import io.grpc.ManagedChannel
import io.grpc.ManagedChannelBuilder
import io.micronaut.context.annotation.Factory
import io.micronaut.grpc.annotation.GrpcChannel
import javax.inject.Singleton

@Factory
class GrpcClientFactory {

    @Singleton
    fun cadastraClientStub(@GrpcChannel("keyManager") channel: ManagedChannel): KeyManagerServiceGrpc.KeyManagerServiceBlockingStub?{

        return KeyManagerServiceGrpc.newBlockingStub(channel)
    }

}