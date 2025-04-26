package kr.kro.btr.config.jwt

import kr.kro.btr.support.TokenDetail
import org.springframework.core.convert.converter.Converter
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import org.springframework.stereotype.Component

@Component
class TokenJwtAuthenticationConverter : Converter<JwtAuthenticationToken, TokenDetail> {

    override fun convert(jwt: JwtAuthenticationToken): TokenDetail? {
        return TokenDetail(jwt)
    }
}