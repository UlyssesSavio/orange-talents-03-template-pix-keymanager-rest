package br.com.zup.edu.controller

import br.com.zup.edu.KeyManagerServiceGrpc
import br.com.zup.edu.KeyPixRequest
import br.com.zup.edu.anotacao.OpenClass
import br.com.zup.edu.tipo
import br.com.zup.edu.tipoChave
import io.grpc.StatusRuntimeException
import io.micronaut.core.annotation.Introspected
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Post
import io.micronaut.http.exceptions.HttpStatusException
import javax.inject.Inject
import javax.validation.Valid
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

@OpenClass
@Controller
@Introspected
class CadastraChaveController(@Inject val gRpcClient: KeyManagerServiceGrpc.KeyManagerServiceBlockingStub){


    @Post("/api/cadastra")
    fun cadastra(@Body @Valid request: CadastraChaveRequest) : HttpResponse<CadastraChaveResponse>{
        val requestGrpc = KeyPixRequest.newBuilder()
            .setChaveASerGerada(request.chave)
            .setIdentificadorCliente(request.idCliente)
            .setTipo(request.tipoConta)
            .setTipoChave(request.tipoChave)
            .build()

        try {
            val cadastra = gRpcClient.cadastra(requestGrpc)
            val cadastraChaveResponse = CadastraChaveResponse(cadastra.chavePix)
            return HttpResponse.created(cadastraChaveResponse)

        }catch (e: StatusRuntimeException){
            val description = e.status.description
            val statusCode = e.status.code

            if(statusCode == io.grpc.Status.Code.NOT_FOUND){
                throw HttpStatusException(HttpStatus.NOT_FOUND, description)
            }

            if(statusCode == io.grpc.Status.Code.INVALID_ARGUMENT){
                throw HttpStatusException(HttpStatus.BAD_REQUEST, description)
            }

            if(statusCode == io.grpc.Status.Code.ALREADY_EXISTS){
                throw HttpStatusException(HttpStatus.FORBIDDEN, description)
            }


            throw  HttpStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.message)//Code+description

        }




    }

}
@OpenClass
@Introspected
data class CadastraChaveResponse(val chavePix:String)

@OpenClass
@Introspected
data class CadastraChaveRequest(@field:NotBlank val idCliente:String,
                                @field:Size(max=77) val chave:String?=null,
                                @field:NotBlank val tipoConta: tipo,
                                @field:NotBlank val tipoChave: tipoChave)