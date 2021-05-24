package br.com.zup.edu.controller

import br.com.zup.edu.*
import br.com.zup.edu.grpcClient.GrpcClientFactory
import com.fasterxml.jackson.databind.ObjectMapper
import com.google.protobuf.Timestamp
import io.grpc.Status
import io.micronaut.context.annotation.Factory
import io.micronaut.context.annotation.Replaces
import io.micronaut.http.HttpRequest
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mock
import org.mockito.Mockito
import java.time.ZoneId
import javax.inject.Inject
import javax.inject.Singleton


@MicronautTest
internal class RemoveControllerTest {

    @Inject
    @field:Client("/")
    lateinit var client: HttpClient


    @Inject
    lateinit var gRpcClientRemove: KeyManagerRemoveGrpc.KeyManagerRemoveBlockingStub

    @AfterEach
    fun antesDeCada() {
        Mockito.reset(gRpcClientRemove)

    }


    @Test
    fun `deve deletar uma chave`() {

        val requestGrpc = KeyRemoveRequest.newBuilder()
            .setIdUsuario("123")
            .setChave("123")
            .build()

        val removeChaveRequest = RemoveChaveRequest(requestGrpc.idUsuario, requestGrpc.chave)

        val responseGrpc = KeyRemoveResponse.newBuilder()
            .setChave(requestGrpc.chave)
            .setIdUsuario(requestGrpc.idUsuario)
            .build()

        Mockito.`when`(gRpcClientRemove.remove(requestGrpc)).thenReturn(responseGrpc)

        val resultado = client.toBlocking()
            .exchange<RemoveChaveRequest, RemoveChaveResponse>(HttpRequest.POST("/api/remove", removeChaveRequest))

        Assertions.assertEquals(resultado.code(), 200)


    }

    @Test
    fun `deve dar chave nao encontrada ao deletar`() {

        val requestGrpc = KeyRemoveRequest.newBuilder()
            .setIdUsuario("123")
            .setChave("123")
            .build()

        val removeChaveRequest = RemoveChaveRequest(requestGrpc.idUsuario, requestGrpc.chave)


        Mockito.`when`(gRpcClientRemove.remove(requestGrpc)).thenThrow(Status.NOT_FOUND.asRuntimeException())

        val resultado = Assertions.assertThrows(HttpClientResponseException::class.java) {
            client.toBlocking()
                .exchange<RemoveChaveRequest, RemoveChaveResponse>(HttpRequest.POST("/api/remove", removeChaveRequest))
        }
        Assertions.assertEquals(resultado.status.code, 404)


    }

    @Test
    fun `deve dar sem permissao para deletar`() {

        val requestGrpc = KeyRemoveRequest.newBuilder()
            .setIdUsuario("123")
            .setChave("123")
            .build()

        val removeChaveRequest = RemoveChaveRequest(requestGrpc.idUsuario, requestGrpc.chave)


        Mockito.`when`(gRpcClientRemove.remove(requestGrpc)).thenThrow(Status.PERMISSION_DENIED.asRuntimeException())

        val resultado = Assertions.assertThrows(HttpClientResponseException::class.java) {
            client.toBlocking()
                .exchange<RemoveChaveRequest, RemoveChaveResponse>(HttpRequest.POST("/api/remove", removeChaveRequest))
        }
        Assertions.assertEquals(resultado.status.code, 406)


    }

    @Test
    fun `deve dar erro interno para deletar`() {

        val requestGrpc = KeyRemoveRequest.newBuilder()
            .setIdUsuario("123")
            .setChave("123")
            .build()

        val removeChaveRequest = RemoveChaveRequest(requestGrpc.idUsuario, requestGrpc.chave)


        Mockito.`when`(gRpcClientRemove.remove(requestGrpc)).thenThrow(Status.INTERNAL.asRuntimeException())

        val resultado = Assertions.assertThrows(HttpClientResponseException::class.java) {
            client.toBlocking()
                .exchange<RemoveChaveRequest, RemoveChaveResponse>(HttpRequest.POST("/api/remove", removeChaveRequest))
        }
        Assertions.assertEquals(resultado.status.code, 500)


    }

    @Test


    fun criaChavePixListaResponse(): ListaChavesPixResponse{
        val chave1 = ListaChavesPixResponse.ChavePix.newBuilder()
            .setChave("123")
            .setCriadaEm(
                Timestamp.newBuilder()
                    .setNanos(123)
                    .setSeconds(123)
                    .build()
            )
            .setPixId("123")
            .setTipoChave(tipoChave.CPF)
            .setTipoConta(tipo.CONTA_CORRENTE)
            .build()

        val chave2 = ListaChavesPixResponse.ChavePix.newBuilder()
            .setChave("123")
            .setCriadaEm(
                Timestamp.newBuilder()
                    .setNanos(123)
                    .setSeconds(123)
                    .build()
            )
            .setPixId("123")
            .setTipoChave(tipoChave.CPF)
            .setTipoConta(tipo.CONTA_CORRENTE)
            .build()

       return ListaChavesPixResponse.newBuilder().addChaves(chave1).addChaves(chave2).setClienteId("123").build()





    }

    fun criaChavePixResponse():CarregaChavePixResponse{
        return CarregaChavePixResponse.newBuilder()
            .setClienteId("123")
            .setPixId("123")
            .setChave(CarregaChavePixResponse.ChavePix.newBuilder()
                .setTipo(tipoChave.CPF)
                .setChave("123")
                .setConta(CarregaChavePixResponse.ChavePix.ContaInfo.newBuilder()
                    .setTipo(tipo.CONTA_CORRENTE)
                    .setInstituicao("itau")
                    .setNomeDoTitular("pedro")
                    .setCpfDoTitular("123")
                    .setAgencia("123")
                    .setNumeroDaConta("123")
                    .build()
                )
                .setCriadaEm(Timestamp.newBuilder().setSeconds(123).setNanos(123).build())
            )
            .build()
    }



    @Factory
    @Replaces(factory = GrpcClientFactory::class)
    internal class factoryMockRemove{
        @Singleton
        fun gRpcClientRemove() = Mockito.mock(KeyManagerRemoveGrpc.KeyManagerRemoveBlockingStub::class.java)
    }



}