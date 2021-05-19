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
internal class ChaveControllerTest {

    @Inject
    @field:Client("/")
    lateinit var client: HttpClient

    @Inject
    lateinit var gRpcClient: KeyManagerServiceGrpc.KeyManagerServiceBlockingStub

    @Inject
    lateinit var gRpcClientRemove: KeyManagerRemoveGrpc.KeyManagerRemoveBlockingStub

    @Inject
    lateinit var gRpcClientBusca: KeyManagerCarregaGrpc.KeyManagerCarregaBlockingStub

    @Inject
    lateinit var gRpcClientLista: KeyManagerListaGrpc.KeyManagerListaBlockingStub

    @AfterEach
    fun antesDeCada() {
        Mockito.reset(gRpcClient)
        Mockito.reset(gRpcClientRemove)
        Mockito.reset(gRpcClientBusca)
        Mockito.reset(gRpcClientLista)

    }

    @Test
    fun `deve cadastrar chave do tipo cpf`() {


        val requestGrpc = KeyPixRequest.newBuilder()
            .setChaveASerGerada("36371803085")
            .setIdentificadorCliente("123")
            .setTipo(tipo.CONTA_CORRENTE)
            .setTipoChave(tipoChave.CPF)
            .build()

        val cadastraChaveRequest = CadastraChaveRequest(
            requestGrpc.identificadorCliente,
            requestGrpc.chaveASerGerada,
            requestGrpc.tipo,
            requestGrpc.tipoChave
        )

        val responseGrpc = KeyPixResponse.newBuilder().setChavePix("36371803085").build()

        Mockito.`when`(gRpcClient.cadastra(requestGrpc)).thenReturn(responseGrpc)

        val resultado = client.toBlocking().exchange<CadastraChaveRequest, CadastraChaveResponse>(
            HttpRequest.POST(
                "/api/cadastra",
                cadastraChaveRequest
            )
        )

        Assertions.assertEquals(resultado.code(), 201)
        // Assertions.assertEquals(resultado.body()!!.chavePix, requestGrpc.chaveASerGerada)


    }

    @Test
    fun `deve dar usuario nao encontrado`() {


        val requestGrpc = KeyPixRequest.newBuilder()
            .setChaveASerGerada("36371803085")
            .setIdentificadorCliente("123")
            .setTipo(tipo.CONTA_CORRENTE)
            .setTipoChave(tipoChave.CPF)
            .build()

        val cadastraChaveRequest = CadastraChaveRequest(
            requestGrpc.identificadorCliente,
            requestGrpc.chaveASerGerada,
            requestGrpc.tipo,
            requestGrpc.tipoChave
        )


        Mockito.`when`(gRpcClient.cadastra(requestGrpc)).thenThrow(Status.NOT_FOUND.asRuntimeException())

        val resultado = Assertions.assertThrows(HttpClientResponseException::class.java) {
            client.toBlocking().exchange<CadastraChaveRequest, CadastraChaveResponse>(
                HttpRequest.POST(
                    "/api/cadastra",
                    cadastraChaveRequest
                )
            )
        }

        Assertions.assertEquals(resultado.status.code, 404)


    }

    @Test
    fun `deve dar chave invalida`() {


        val requestGrpc = KeyPixRequest.newBuilder()
            .setChaveASerGerada("36371803085")
            .setIdentificadorCliente("123")
            .setTipo(tipo.CONTA_CORRENTE)
            .setTipoChave(tipoChave.CPF)
            .build()

        val cadastraChaveRequest = CadastraChaveRequest(
            requestGrpc.identificadorCliente,
            requestGrpc.chaveASerGerada,
            requestGrpc.tipo,
            requestGrpc.tipoChave
        )


        Mockito.`when`(gRpcClient.cadastra(requestGrpc)).thenThrow(Status.INVALID_ARGUMENT.asRuntimeException())

        val resultado = Assertions.assertThrows(HttpClientResponseException::class.java) {
            client.toBlocking().exchange<CadastraChaveRequest, CadastraChaveResponse>(
                HttpRequest.POST(
                    "/api/cadastra",
                    cadastraChaveRequest
                )
            )
        }

        Assertions.assertEquals(resultado.status.code, 400)


    }

    @Test
    fun `deve dar chave ja cadastrada`() {


        val requestGrpc = KeyPixRequest.newBuilder()
            .setChaveASerGerada("36371803085")
            .setIdentificadorCliente("123")
            .setTipo(tipo.CONTA_CORRENTE)
            .setTipoChave(tipoChave.CPF)
            .build()

        val cadastraChaveRequest = CadastraChaveRequest(
            requestGrpc.identificadorCliente,
            requestGrpc.chaveASerGerada,
            requestGrpc.tipo,
            requestGrpc.tipoChave
        )


        Mockito.`when`(gRpcClient.cadastra(requestGrpc)).thenThrow(Status.ALREADY_EXISTS.asRuntimeException())

        val resultado = Assertions.assertThrows(HttpClientResponseException::class.java) {
            client.toBlocking().exchange<CadastraChaveRequest, CadastraChaveResponse>(
                HttpRequest.POST(
                    "/api/cadastra",
                    cadastraChaveRequest
                )
            )
        }

        Assertions.assertEquals(resultado.status.code, 403)


    }

    @Test
    fun `deve dar erro interno`() {


        val requestGrpc = KeyPixRequest.newBuilder()
            .setChaveASerGerada("36371803085")
            .setIdentificadorCliente("123")
            .setTipo(tipo.CONTA_CORRENTE)
            .setTipoChave(tipoChave.CPF)
            .build()

        val cadastraChaveRequest = CadastraChaveRequest(
            requestGrpc.identificadorCliente,
            requestGrpc.chaveASerGerada,
            requestGrpc.tipo,
            requestGrpc.tipoChave
        )


        Mockito.`when`(gRpcClient.cadastra(requestGrpc)).thenThrow(Status.INTERNAL.asRuntimeException())

        val resultado = Assertions.assertThrows(HttpClientResponseException::class.java) {
            client.toBlocking().exchange<CadastraChaveRequest, CadastraChaveResponse>(
                HttpRequest.POST(
                    "/api/cadastra",
                    cadastraChaveRequest
                )
            )
        }

        Assertions.assertEquals(resultado.status.code, 500)


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
    fun `deve buscar uma chave cadastrada`() {
        val carregaChavePixResponse = criaChavePixResponse()

        val grpcRequest = CarregaChavePixRequest.newBuilder().setChave("123").build()

        Mockito.`when`(gRpcClientBusca.carrega(grpcRequest)).thenReturn(carregaChavePixResponse)

        val resultado = client.toBlocking().exchange<String, BuscaChaveResponse>(HttpRequest.GET("/api/busca/123"))

        Assertions.assertEquals(resultado.code(), 200)
    }

    @Test
    fun `deve nao encontrar chave buscada`() {

        val grpcRequest = CarregaChavePixRequest.newBuilder().setChave("123").build()

        Mockito.`when`(gRpcClientBusca.carrega(grpcRequest)).thenThrow(Status.NOT_FOUND.asRuntimeException())

        val resultado = Assertions.assertThrows(HttpClientResponseException::class.java) {
            client.toBlocking().exchange<String, BuscaChaveResponse>(HttpRequest.GET("/api/busca/123"))
        }

        Assertions.assertEquals(resultado.status.code, 404)

    }

    @Test
    fun `deve nao erro interno ao buscar chave por chave`() {

        val grpcRequest = CarregaChavePixRequest.newBuilder().setChave("123").build()

        Mockito.`when`(gRpcClientBusca.carrega(grpcRequest)).thenThrow(Status.INTERNAL.asRuntimeException())

        val resultado = Assertions.assertThrows(HttpClientResponseException::class.java) {
            client.toBlocking().exchange<String, BuscaChaveResponse>(HttpRequest.GET("/api/busca/123"))
        }

        Assertions.assertEquals(resultado.status.code, 500)

    }

    @Test
    fun `deve buscar uma chave cadastrada por pix id e client id`() {
        val carregaChavePixResponse = criaChavePixResponse()

        val grpcRequest = CarregaChavePixRequest.newBuilder().setPixId(
            CarregaChavePixRequest.newBuilder()
                .pixIdBuilder.setClientId("123")
                .setPixId("123").build()
        ).build()

        Mockito.`when`(gRpcClientBusca.carrega(grpcRequest)).thenReturn(carregaChavePixResponse)

        val resultado = client.toBlocking().exchange<String, BuscaChaveResponse>(HttpRequest.GET("/api/busca/123/123"))

        Assertions.assertEquals(resultado.code(), 200)

    }

    @Test
    fun `deve nao buscar uma chave cadastrada por pix id e client id nao encontrada`() {

        val grpcRequest = CarregaChavePixRequest.newBuilder().setPixId(
            CarregaChavePixRequest.newBuilder()
                .pixIdBuilder.setClientId("123")
                .setPixId("123").build()
        ).build()

        Mockito.`when`(gRpcClientBusca.carrega(grpcRequest)).thenThrow(Status.NOT_FOUND.asRuntimeException())

        val resultado = Assertions.assertThrows(HttpClientResponseException::class.java) {
            client.toBlocking().exchange<String, BuscaChaveResponse>(HttpRequest.GET("/api/busca/123/123"))
        }

        Assertions.assertEquals(resultado.status.code, 404)
    }

    @Test
    fun `deve nao buscar uma chave cadastrada por pix id e client id erro interno`() {

        val grpcRequest = CarregaChavePixRequest.newBuilder().setPixId(
            CarregaChavePixRequest.newBuilder()
                .pixIdBuilder.setClientId("123")
                .setPixId("123").build()
        ).build()

        Mockito.`when`(gRpcClientBusca.carrega(grpcRequest)).thenThrow(Status.INTERNAL.asRuntimeException())

        val resultado = Assertions.assertThrows(HttpClientResponseException::class.java) {
            client.toBlocking().exchange<String, BuscaChaveResponse>(HttpRequest.GET("/api/busca/123/123"))
        }

        Assertions.assertEquals(resultado.status.code, 500)
    }

    @Test
    fun `deve buscar lista de chaves`(){

        val requestGrpc = ListaChavesPixRequest.newBuilder().setClientId("123").build()
        val responseGrpc = criaChavePixListaResponse()

        Mockito.`when`(gRpcClientLista.lista(requestGrpc)).thenReturn(responseGrpc)

        //val resultado = client.toBlocking().exchange<String, ListaChaveResponse>(HttpRequest.GET("/api/lista/123"))
        val resultado =
            client.toBlocking().exchange<String,ListaChaveResponse>(HttpRequest.GET("/api/lista/123"),
                ListaChaveResponse::class.java)

        Assertions.assertEquals(resultado!!.status.code, 200)
        Assertions.assertEquals(resultado.body().chaves!!.size, 2)

    }


    @Test
    fun `deve buscar lista vazia de chaves`(){

        val requestGrpc = ListaChavesPixRequest.newBuilder().setClientId("123").build()

        Mockito.`when`(gRpcClientLista.lista(requestGrpc)).thenReturn(ListaChavesPixResponse.newBuilder()
            .setClienteId("123").build())

        val resultado =
            client.toBlocking().exchange<String,ListaChaveResponse>(HttpRequest.GET("/api/lista/123"),
                ListaChaveResponse::class.java)

        Assertions.assertEquals(resultado.status.code, 200)
        Assertions.assertNull(resultado.body().chaves)

    }


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
    internal class factoryMockLista{
        @Singleton
        fun gRpcClientLista() = Mockito.mock(KeyManagerListaGrpc.KeyManagerListaBlockingStub::class.java)
    }
    @Factory
    @Replaces(factory = GrpcClientFactory::class)
    internal class factoryMockBusca{
        @Singleton
        fun gRpcClientBusca() = Mockito.mock(KeyManagerCarregaGrpc.KeyManagerCarregaBlockingStub::class.java)
    }

    @Factory
    @Replaces(factory = GrpcClientFactory::class)
    internal class factoryMockRemove{
        @Singleton
        fun gRpcClientRemove() = Mockito.mock(KeyManagerRemoveGrpc.KeyManagerRemoveBlockingStub::class.java)
    }


    @Factory
    @Replaces(factory = GrpcClientFactory::class)
    internal class factoryMock{
        @Singleton
        fun gRpcClient() = Mockito.mock(KeyManagerServiceGrpc.KeyManagerServiceBlockingStub::class.java)
    }


}