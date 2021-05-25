package br.com.zup.edu.erro

import io.micronaut.context.annotation.Replaces
import io.micronaut.context.annotation.Requires
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.annotation.Error
import io.micronaut.http.annotation.Produces
import io.micronaut.http.hateoas.JsonError
import io.micronaut.http.hateoas.Link
import io.micronaut.validation.exceptions.ConstraintExceptionHandler



class ConstraintExceptionHandler : ConstraintExceptionHandler() {

}

//isso so funciona na classe q ela foi criada
@Error(status = HttpStatus.NOT_FOUND, global = true)
fun notFound(request: HttpRequest<*>): HttpResponse<ErroPadrao> {
    return  HttpResponse.notFound<ErroPadrao?>().body(ErroPadrao("Nao encontrado"))
}



data class ErroPadrao(val mensagem:String)