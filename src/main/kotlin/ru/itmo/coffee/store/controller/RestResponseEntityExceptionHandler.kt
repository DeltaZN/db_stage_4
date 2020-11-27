package ru.itmo.coffee.store.controller

import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler


@ControllerAdvice
class RestResponseEntityExceptionHandler : ResponseEntityExceptionHandler() {
    @ExceptionHandler(value = [EmptyResultDataAccessException::class])
    protected fun handleNotFound(
            ex: EmptyResultDataAccessException, request: WebRequest): ResponseEntity<Any?>? {
        val bodyOfResponse = "Entity not found"
        return handleExceptionInternal(ex, bodyOfResponse,
                HttpHeaders(), HttpStatus.NOT_FOUND, request)
    }
}