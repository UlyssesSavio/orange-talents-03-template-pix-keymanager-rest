package br.com.zup.edu.grpcClient

import br.com.zup.edu.KeyManagerCarregaGrpc
import br.com.zup.edu.KeyManagerListaGrpc
import br.com.zup.edu.KeyManagerRemoveGrpc
import br.com.zup.edu.KeyManagerServiceGrpc
import io.grpc.ManagedChannel
import io.grpc.ManagedChannelBuilder
import io.grpc.stub.AbstractBlockingStub
import io.micronaut.context.annotation.Factory
import io.micronaut.grpc.annotation.GrpcChannel
import javax.inject.Singleton

@Factory
class GrpcClientFactory {

    @Singleton
    fun cadastraClientStub(@GrpcChannel("keyManager") channel: ManagedChannel): KeyManagerServiceGrpc.KeyManagerServiceBlockingStub?{


        return KeyManagerServiceGrpc.newBlockingStub(channel)
    }

    @Singleton
    fun removeClientStub(@GrpcChannel("keyManager") channel: ManagedChannel): KeyManagerRemoveGrpc.KeyManagerRemoveBlockingStub?{
        return KeyManagerRemoveGrpc.newBlockingStub(channel)

    }

    @Singleton
    fun buscaClientStub(@GrpcChannel("keyManager") channel: ManagedChannel): KeyManagerCarregaGrpc.KeyManagerCarregaBlockingStub?{
        return KeyManagerCarregaGrpc.newBlockingStub(channel)
    }

    @Singleton
    fun listaClientStub(@GrpcChannel("keyManager") channel: ManagedChannel): KeyManagerListaGrpc.KeyManagerListaBlockingStub?{
        return KeyManagerListaGrpc.newBlockingStub(channel)
    }


}