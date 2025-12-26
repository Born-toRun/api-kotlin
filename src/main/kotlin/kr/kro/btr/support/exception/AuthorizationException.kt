package kr.kro.btr.support.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(code = HttpStatus.UNAUTHORIZED)
class AuthorizationException(override val message: String) : RuntimeException(message)
