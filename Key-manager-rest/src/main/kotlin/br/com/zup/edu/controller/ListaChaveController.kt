package br.com.zup.edu.controller

import br.com.zup.edu.*
import br.com.zup.edu.anotacao.OpenClass
import br.com.zup.edu.erro.ErrorHandler
import io.micronaut.core.annotation.Introspected
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.QueryValue
import java.time.LocalDateTime
import java.time.ZoneOffset
import javax.inject.Inject

@ErrorHandler
@OpenClass
@Controller
class ListaChaveController(@Inject val gRpcClient: KeyManagerListaGrpc.KeyManagerListaBlockingStub) {

    @Get("/api/lista/{idClient}")
    fun listaChave(@QueryValue idClient:String): HttpResponse<ListaChaveResponse> {

        val grpcRequest = ListaChavesPixRequest.newBuilder().setClientId(idClient).build()

        val lista = gRpcClient.lista(grpcRequest)
        val listaMap = lista.chavesList.map { ChaveListaResponse.toModel(it)}

        return HttpResponse.ok(ListaChaveResponse(listaMap, grpcRequest.clientId))

    }


}


@OpenClass
data class ListaChaveResponse(val chaves:List<ChaveListaResponse>?, val clientId:String)
@OpenClass
data class ChaveListaResponse(val pixId:String, val tipoChave: tipoChave, val chave:String, val tipoConta: tipo, val criadaEm: LocalDateTime ){
    companion object{
        fun toModel(chavePix:ListaChavesPixResponse.ChavePix):ChaveListaResponse{
            return ChaveListaResponse(chavePix.pixId, chavePix.tipoChave,chavePix.chave, chavePix.tipoConta, LocalDateTime.ofEpochSecond(
                chavePix.criadaEm.nanos.toLong(), chavePix.criadaEm.seconds.toInt(), ZoneOffset.UTC))
        }
    }
}


