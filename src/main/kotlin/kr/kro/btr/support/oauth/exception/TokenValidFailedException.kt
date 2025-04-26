package kr.kro.btr.support.oauth.exception

import java.lang.RuntimeException

class TokenValidFailedException : RuntimeException {
    constructor() : super("Failed to generate Token.")

    private constructor(message: String) : super(message)
}
