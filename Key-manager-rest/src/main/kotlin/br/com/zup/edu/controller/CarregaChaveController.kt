package br.com.zup.edu.controller

import br.com.zup.edu.*
import br.com.zup.edu.anotacao.OpenClass
import io.grpc.StatusRuntimeException
import io.micronaut.core.annotation.Introspected
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.*
import io.micronaut.http.exceptions.HttpStatusException
import java.time.LocalDateTime
import java.time.ZoneOffset
import javax.inject.Inject
import javax.validation.Valid
import javax.validation.constraints.NotBlank

@OpenClass
@Controller
@Introspected
class CarregaChaveController(@Inject val gRpcClient: KeyManagerCarregaGrpc.KeyManagerCarregaBlockingStub) {

    @Get("/api/busca/{request}")
    fun buscaPorChave(@QueryValue request:String): HttpResponse<BuscaChaveResponse> {

        val grpcRequest = CarregaChavePixRequest.newBuilder().setChave(request).build()

        try {
            val responseGrpc = gRpcClient.carrega(grpcRequest)
            val responseConvertido = converterResponseGrpcToModel(responseGrpc)

            return HttpResponse.ok(responseConvertido)

        }catch (e: StatusRuntimeException){
            val description = e.status.description
            val statusCode = e.status.code

            if(statusCode == io.grpc.Status.Code.NOT_FOUND){
                throw HttpStatusException(HttpStatus.NOT_FOUND, description)
            }
            throw  HttpStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.message)//Code+description

        }

    }

    @Get("/api/busca/{pixId}/{clientId}")
    fun buscaPorPixId(@QueryValue pixId: String, @QueryValue clientId: String): HttpResponse<BuscaChaveResponse>{

        val grpcRequest = CarregaChavePixRequest.newBuilder().setPixId(CarregaChavePixRequest.newBuilder()
            .pixIdBuilder.setClientId(clientId)
            .setPixId(pixId).build()).build()


        try{
            val responseGrpc = gRpcClient.carrega(grpcRequest)
            val responseConvertido = converterResponseGrpcToModel(responseGrpc)

            return HttpResponse.ok(responseConvertido)


        }catch (e: StatusRuntimeException){
            val description = e.status.description
            val statusCode = e.status.code

            if(statusCode == io.grpc.Status.Code.NOT_FOUND){
                throw HttpStatusException(HttpStatus.NOT_FOUND, description)
            }
            throw  HttpStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.message)//Code+description
        }



    }




    fun converterResponseGrpcToModel( grpc :CarregaChavePixResponse): BuscaChaveResponse{
        return BuscaChaveResponse(grpc.clienteId,
            grpc.pixId,Conta(
                grpc.chave.conta.tipo,
                grpc.chave.conta.instituicao,
                grpc.chave.conta.nomeDoTitular,
                grpc.chave.conta.cpfDoTitular,
                grpc.chave.conta.agencia,
                grpc.chave.conta.numeroDaConta),
            Chave(grpc.chave.tipo, grpc.chave.chave),
        LocalDateTime.ofEpochSecond(grpc.chave.criadaEm.seconds, grpc.chave.criadaEm.nanos, ZoneOffset.UTC))
    }

}



@OpenClass
@Introspected
data class Conta(val tipo:tipo, val instituicao:String, val nomeDoTitular:String, val cpfDoTitular:String, val agencia:String, val numeroDaConta:String)

@OpenClass
@Introspected
data class Chave(val tipo: tipoChave, val chave:String)


@OpenClass
@Introspected
data class BuscaChaveResponse(val clienteId:String,
                              val pixId:String,
                              val conta: Conta,
                              val chave: Chave,
                              val criadaEm: LocalDateTime
                              )