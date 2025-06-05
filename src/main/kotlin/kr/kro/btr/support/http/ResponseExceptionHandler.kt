package kr.kro.btr.support.http

import kr.kro.btr.support.Notification
import kr.kro.btr.support.exception.*
import kr.kro.btr.support.http.model.ExceptionResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class ResponseExceptionHandler(
    private val notification: Notification
) {

    @ExceptionHandler(NotFoundException::class)
    fun handleNotFound(ex: NotFoundException): ResponseEntity<ExceptionResponse> {
        return handleException(HttpStatus.NOT_FOUND, ex)
    }

    @ExceptionHandler(NetworkException::class, ClientUnknownException::class)
    fun handleBadGateway(ex: Exception): ResponseEntity<ExceptionResponse> {
        notify(ex, HttpStatus.BAD_GATEWAY)
        return handleException(HttpStatus.BAD_GATEWAY, ex)
    }

    @ExceptionHandler(CryptoException::class, InternalServerException::class, NullPointerException::class)
    fun handleInternalServerError(ex: Exception): ResponseEntity<ExceptionResponse> {
        notify(ex, HttpStatus.INTERNAL_SERVER_ERROR)
        return handleException(HttpStatus.INTERNAL_SERVER_ERROR, ex)
    }

    @ExceptionHandler(InvalidException::class, DuplicationException::class)
    fun handleBadRequest(ex: Exception): ResponseEntity<ExceptionResponse> {
        return handleException(HttpStatus.BAD_REQUEST, ex)
    }

    @ExceptionHandler(AuthorizationException::class)
    fun handleUnauthorized(ex: AuthorizationException): ResponseEntity<ExceptionResponse> {
        return handleException(HttpStatus.UNAUTHORIZED, ex)
    }

    @ExceptionHandler(ForBiddenException::class, InvalidTokenException::class)
    fun handleForBidden(ex: Exception): ResponseEntity<ExceptionResponse> {
        return handleException(HttpStatus.FORBIDDEN, ex)
    }

    private fun notify(ex: Exception, status: HttpStatus) {
        val embedFieldMap = mapOf("trace" to ex.stackTraceToString())
        notification.send(
            "httpStatus[${status.value()}:${status.reasonPhrase}] 발생.",
            ex.toString(),
            embedFieldMap
        )
    }

    private fun handleException(status: HttpStatus, ex: Exception): ResponseEntity<ExceptionResponse> {
        ex.printStackTrace()
        return ResponseEntity.status(status)
            .body(ExceptionResponse(message = ex.message))
    }
}
