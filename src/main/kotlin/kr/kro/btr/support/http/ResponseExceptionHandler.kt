package kr.kro.btr.support.http

import kr.kro.btr.support.Notification
import kr.kro.btr.support.exception.*
import kr.kro.btr.support.http.model.ExceptionResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class ResponseExceptionHandler(private val notification: Notification) {

    @ExceptionHandler(Exception::class)
    fun handleDefault(ex: Exception ): ResponseEntity<ExceptionResponse> {
        ex.printStackTrace()
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(ExceptionResponse(message = "시스템 오류입니다."))
    }

    @ExceptionHandler(NotFoundException::class)
    fun handleNotFound(ex: NotFoundException ): ResponseEntity<ExceptionResponse> {
        ex.printStackTrace()
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(ExceptionResponse(message = ex.message))
    }

    @ExceptionHandler(NetworkException::class, ClientUnknownException::class)
    fun handleBadGateway(ex: Exception): ResponseEntity<ExceptionResponse> {
        ex.printStackTrace()
        notification.send(ex.toString())
        return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
            .body(ExceptionResponse(message = ex.message))
    }

    @ExceptionHandler(CryptoException::class, InternalServerException::class, NullPointerException::class)
    fun handleInternalServerError(ex: Exception): ResponseEntity<ExceptionResponse> {
        ex.printStackTrace()
        notification.send(ex.toString())
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(ExceptionResponse(message = ex.message))
    }

    @ExceptionHandler(InvalidException::class, DuplicationException::class)
    fun handleBadRequest(ex: Exception): ResponseEntity<ExceptionResponse> {
        ex.printStackTrace()
        return ResponseEntity.badRequest()
            .body(ExceptionResponse(message = ex.message))
    }

    @ExceptionHandler(AuthorizationException::class)
    fun handleUnauthorized(ex: AuthorizationException): ResponseEntity<ExceptionResponse> {
        ex.printStackTrace()
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
            .body(ExceptionResponse(message = ex.message))
    }

    @ExceptionHandler(ForBiddenException::class, InvalidTokenException::class)
    fun handleForBidden(ex: ForBiddenException): ResponseEntity<ExceptionResponse> {
        ex.printStackTrace()
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
            .body(ExceptionResponse(message = ex.message))
    }
}
