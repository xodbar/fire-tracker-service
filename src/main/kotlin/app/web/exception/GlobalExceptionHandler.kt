package app.web.exception

import app.core.exception.InvalidTokenException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(InvalidTokenException::class)
    fun handleInvalidTokenException(ex: InvalidTokenException): ResponseEntity<ErrorResponse> {
        val errorResponse = ErrorResponse(ex.message, HttpStatus.FORBIDDEN.value())
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse)
    }

    data class ErrorResponse(val message: String, val status: Int)
}
