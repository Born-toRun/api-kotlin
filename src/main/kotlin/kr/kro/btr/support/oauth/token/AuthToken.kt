package kr.kro.btr.support.oauth.token

import io.github.oshai.kotlinlogging.KotlinLogging
import io.jsonwebtoken.*
import io.jsonwebtoken.security.SecurityException
import io.jsonwebtoken.security.SignatureException
import kr.kro.btr.support.exception.InvalidTokenException
import java.util.*
import javax.crypto.SecretKey

class AuthToken(
    val token: String,
    private val key: SecretKey
) {

    constructor(id: Long, expiry: Date, key: SecretKey) : this(
        token = Jwts.builder()
            .subject(id.toString())
            .signWith(key, Jwts.SIG.HS512)
            .expiration(expiry)
            .compact(),
        key = key
    )

    constructor(id: Long, userName: String?, crewId: Long?, managedCrewId: Long?, role: String, expiry: Date, key: SecretKey) : this(
        token = Jwts.builder()
            .subject(id.toString())
            .claim(AUTHORITIES_KEY, role)
            .claim(USER_NAME_KEY, userName)
            .claim(CREW_ID_KEY, crewId)
            .claim(MANAGED_CREW_ID_KEY, managedCrewId)
            .signWith(key, Jwts.SIG.HS512)
            .expiration(expiry)
            .compact(),
        key = key
    )

    fun isValidate(): Boolean {
        return try {
            validate()
            true
        } catch (e: InvalidTokenException) {
            false
        }
    }

    fun validate() {
        try {
            Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .payload
        } catch (e: SecurityException) {
            throw InvalidTokenException("Invalid JWT signature.")
        } catch (e: SignatureException) {
            throw InvalidTokenException("Invalid JWT signature.")
        } catch (e: MalformedJwtException) {
            throw InvalidTokenException("Invalid JWT token.")
        } catch (e: ExpiredJwtException) {
            throw InvalidTokenException("Expired JWT token.")
        } catch (e: UnsupportedJwtException) {
            throw InvalidTokenException("Unsupported JWT token.")
        } catch (e: IllegalArgumentException) {
            throw InvalidTokenException("JWT token compact of handler are invalid.")
        }
    }

    fun getExpiredTokenClaims(): Claims? {
        return try {
            Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .payload
        } catch (e: ExpiredJwtException) {
            log.info { "Expired JWT token." }
            e.claims
        } catch (e: Exception) {
            log.warn(e) { "Failed to get claims from token: $e" }
            null
        }
    }

    fun getTokenClaims(): Claims? {
        return try {
            Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .payload
        } catch (e: SecurityException) {
            log.warn {"Invalid JWT signature." }
            null
        } catch (e: MalformedJwtException) {
            log.warn {"Invalid JWT token." }
            null
        } catch (e: ExpiredJwtException) {
            log.warn {"Expired JWT token." }
            null
        } catch (e: UnsupportedJwtException) {
            log.warn {"Unsupported JWT token." }
            null
        } catch (e: IllegalArgumentException) {
            log.warn {"JWT token compact of handler are invalid." }
            null
        }
    }

    companion object {
        private val log = KotlinLogging.logger {}
        const val AUTHORITIES_KEY = "role"
        const val USER_NAME_KEY = "userName"
        const val CREW_ID_KEY = "crewId"
        const val MANAGED_CREW_ID_KEY = "managedCrewId"

        fun from(token: String, key: SecretKey): AuthToken {
            return AuthToken(token, key)
        }

        fun create(id: Long, expiry: Date, key: SecretKey): AuthToken {
            val token = Jwts.builder()
                .subject(id.toString())
                .signWith(key, Jwts.SIG.HS512)
                .expiration(expiry)
                .compact()
            return AuthToken(token, key)
        }

        fun create(id: Long, userName: String, crewId: Long?, managedCrewId: Long?, role: String, expiry: Date, key: SecretKey): AuthToken {
            val token = Jwts.builder()
                .subject(id.toString())
                .claim(AUTHORITIES_KEY, role)
                .claim(USER_NAME_KEY, userName)
                .claim(CREW_ID_KEY, crewId)
                .claim(MANAGED_CREW_ID_KEY, managedCrewId)
                .signWith(key, Jwts.SIG.HS512)
                .expiration(expiry)
                .compact()
            return AuthToken(token, key)
        }
    }
}
