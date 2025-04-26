package kr.kro.btr.config.properties

import org.springframework.boot.context.properties.ConfigurationProperties



@ConfigurationProperties(prefix = "app")
class AppProperties {

    val auth: Auth = Auth()
    val oauth2: OAuth2 = OAuth2()

    data class Auth(
        var tokenSecret: String? = null,
        var tokenExpiry: Long = 0,
        var refreshTokenExpiry: Long = 0
    )

    class OAuth2 {
        var authorizedRedirectUris: MutableList<String> = ArrayList()

        fun authorizedRedirectUris(authorizedRedirectUris: List<String>): OAuth2 {
            this.authorizedRedirectUris = authorizedRedirectUris.toMutableList()
            return this
        }
    }
}
