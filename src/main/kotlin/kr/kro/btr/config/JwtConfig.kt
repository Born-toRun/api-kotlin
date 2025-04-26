package kr.kro.btr.config

import io.github.oshai.kotlinlogging.KotlinLogging
import kr.kro.btr.support.oauth.token.AuthTokenProvider
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.security.NoSuchAlgorithmException
import javax.crypto.KeyGenerator

@Configuration
class JwtConfig {

    @Bean
    fun jwtProvider(): AuthTokenProvider {
        return try {
            val keyGenerator = KeyGenerator.getInstance(ALGORITHM)
            keyGenerator.init(512)
            AuthTokenProvider(keyGenerator)
        } catch (e: NoSuchAlgorithmException) {
            log.error { "$ALGORITHM algorithm이 제공되지 않습니다. $e" }
            throw RuntimeException(e)
        }
    }

    companion object {
        private const val ALGORITHM = "HmacSHA256"
        private val log = KotlinLogging.logger {}
    }
}
