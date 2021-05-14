package br.com.zup.edu.controller

import br.com.zup.edu.KeyManagerRemoveGrpc
import br.com.zup.edu.KeyRemoveRequest
import br.com.zup.edu.anotacao.OpenClass
import io.grpc.StatusRuntimeException
import io.micronaut.core.annotation.Introspected
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Post
import io.micronaut.http.exceptions.HttpStatusException
import javax.inject.Inject
import javax.validation.Valid
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size


@OpenClass
@Controller
@Introspected
class RemoveChaveController(@Inject val gRpcClient:KeyManagerRemoveGrpc.KeyManagerRemoveBlockingStub) {


    @Post("/api/remove")
    fun remove(@Body @Valid request: RemoveChaveRequest): HttpResponse<RemoveChaveResponse>{

        val requestGrpc = KeyRemoveRequest.newBuilder()
            .setChave(request.chave)
            .setIdUsuario(request.idUsuario)
            .build()

        try{

            val responseGrpc = gRpcClient.remove(requestGrpc)
            return HttpResponse.ok(RemoveChaveResponse(responseGrpc.idUsuario, responseGrpc.chave))

        }catch (e: StatusRuntimeException){
            val description = e.status.description
            val statusCode = e.status.code

            if(statusCode == io.grpc.Status.Code.NOT_FOUND){
                throw HttpStatusException(HttpStatus.NOT_FOUND, description)
            }

            if(statusCode == io.grpc.Status.Code.PERMISSION_DENIED){
                throw HttpStatusException(HttpStatus.NOT_ACCEPTABLE, description)
            }



            throw  HttpStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.message)//Code+description


        }


    }

}

@OpenClass
@Introspected
data class RemoveChaveRequest(@field:NotBlank val idUsuario:String, @field:NotBlank @field:Size(max=77) val chave:String)

@OpenClass
@Introspected
data class RemoveChaveResponse(@field:NotBlank val idUsuario:String, @field:NotBlank @field:Size(max=77) val chave:String)