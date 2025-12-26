package kr.kro.btr.support.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(code = HttpStatus.FORBIDDEN)
class ForBiddenException(override val message: String) : RuntimeException(message)
