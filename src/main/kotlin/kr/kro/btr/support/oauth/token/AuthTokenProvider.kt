package kr.kro.btr.support.oauth.token

import java.util.Date
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.oauth2.jose.jws.MacAlgorithm
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.oauth2.jwt.JwtDecoder
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import io.jsonwebtoken.Claims
import kr.kro.btr.support.oauth.exception.TokenValidFailedException
import kr.kro.btr.support.oauth.token.AuthToken
import org.slf4j.LoggerFactory

class AuthTokenProvider(keyGenerator: KeyGenerator) {

    private val secretKey: SecretKey = keyGenerator.generateKey()
    private val logger = LoggerFactory.getLogger(AuthTokenProvider::class.java)

    fun createAuthToken(id: Long, expiry: Date): AuthToken {
        return AuthToken(id, expiry, secretKey)
    }

    fun createAuthToken(id: Long, userName: String, crewId: Long?, role: String, expiry: Date): AuthToken {
        return AuthToken(id, userName, crewId, role, expiry, secretKey)
    }

    fun convertAuthToken(token: String): AuthToken {
        return AuthToken(token, secretKey)
    }

    fun getAuthentication(authToken: AuthToken): Authentication {
        val jwtDecoder: JwtDecoder = NimbusJwtDecoder.withSecretKey(secretKey)
            .macAlgorithm(MacAlgorithm.HS512)
            .build()

        if (authToken.isValidate()) {
            val claims: Claims = authToken.getTokenClaims()!!
            val authorities: Collection<GrantedAuthority> = claims[AuthToken.AUTHORITIES_KEY]
                .toString()
                .split(",")
                .map { SimpleGrantedAuthority(it) }

            logger.debug("claims subject := [{}]", claims.subject)

            val jwt: Jwt = jwtDecoder.decode(authToken.token)
            return JwtAuthenticationToken(jwt, authorities)
        } else {
            throw TokenValidFailedException()
        }
    }
}
