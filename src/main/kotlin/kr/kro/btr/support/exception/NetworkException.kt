package kr.kro.btr.support.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(code = HttpStatus.BAD_GATEWAY)
class NetworkException(override val message: String) : RuntimeException(message)
