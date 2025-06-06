package kr.kro.btr.support.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
class CryptoException(override val message: String) : RuntimeException(message)