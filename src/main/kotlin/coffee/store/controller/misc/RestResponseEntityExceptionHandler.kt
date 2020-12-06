package coffee.store.controller.misc

import coffee.store.payload.response.MessageResponse
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import javax.persistence.EntityNotFoundException


@ControllerAdvice
class RestResponseEntityExceptionHandler : ResponseEntityExceptionHandler() {
    @ExceptionHandler(value = [EmptyResultDataAccessException::class, EntityNotFoundException::class, UsernameNotFoundException::class])
    protected fun handleNotFound(
            ex: Exception, request: WebRequest): ResponseEntity<Any?>? {
        return handleExceptionInternal(ex, MessageResponse(ex.message!!),
                HttpHeaders(), HttpStatus.NOT_FOUND, request)
    }
}