package com.itmo.blss.api

import com.itmo.blss.api.response.ErrorMessage
import org.postgresql.util.PSQLException
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException
import java.lang.IllegalArgumentException
import java.sql.SQLException

@RestControllerAdvice
class ControllerAdvice {

    @ExceptionHandler(IllegalArgumentException::class)
    fun illegalArgumentException(exception: IllegalArgumentException): ResponseEntity<ErrorMessage> {
        return ResponseEntity
            .badRequest()
            .body(ErrorMessage(exception.message))
    }

    @ExceptionHandler(PSQLException::class)
    fun pSQLException(exception: PSQLException): ResponseEntity<ErrorMessage> {
        return ResponseEntity.badRequest()
            .body(ErrorMessage(exception.message))
    }
}