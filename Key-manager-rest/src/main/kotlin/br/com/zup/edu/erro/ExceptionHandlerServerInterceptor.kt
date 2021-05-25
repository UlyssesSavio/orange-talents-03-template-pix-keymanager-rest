package br.com.zup.edu.erro



import io.grpc.Status
import io.grpc.StatusRuntimeException
import io.micronaut.aop.InterceptorBean
import io.micronaut.aop.MethodInterceptor
import io.micronaut.aop.MethodInvocationContext
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.exceptions.HttpStatusException
import io.micronaut.http.hateoas.JsonError
import io.micronaut.http.server.exceptions.ExceptionHandler;
import org.slf4j.LoggerFactory
import javax.inject.Singleton


@Singleton
@InterceptorBean(ErrorHandler::class)
class ExceptionHandlerServerInterceptor : MethodInterceptor<Any, Any> {
    override fun intercept(context: MethodInvocationContext<Any, Any?>): Any? {
        try{
            return context.proceed()
        } catch (e: HttpStatusException) {
           return  HttpResponse.status<ErroPadrao>(e.status).body(e.message?.let { ErroPadrao(it) })
        }
    }
}


