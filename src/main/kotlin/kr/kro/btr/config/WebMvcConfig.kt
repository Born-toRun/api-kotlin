package kr.kro.btr.config

import kr.kro.btr.config.jwt.AuthenticationPrincipalArgumentResolver
import kr.kro.btr.config.jwt.TokenAuthenticationPrincipalArgumentResolver
import kr.kro.btr.config.properties.CorsProperties
import kr.kro.btr.support.TokenDetail
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Configuration
import org.springframework.core.convert.converter.Converter
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
@EnableConfigurationProperties(CorsProperties::class)
class WebMvcConfig(
    private val jwtToTokenConverter: Converter<JwtAuthenticationToken, TokenDetail>,
    private val corsProperties: CorsProperties) : WebMvcConfigurer {

    override fun addArgumentResolvers(resolvers: MutableList<HandlerMethodArgumentResolver>) {
        resolvers.add(TokenAuthenticationPrincipalArgumentResolver(jwtToTokenConverter))
        resolvers.add(AuthenticationPrincipalArgumentResolver())
    }

    override fun addCorsMappings(registry: CorsRegistry) {
        registry.addMapping("/**")
            .allowedOrigins(*corsProperties.allowedOrigins.split(",").toTypedArray())
            .allowedHeaders(*corsProperties.allowedHeaders.split(",").toTypedArray())
            .allowedMethods(*corsProperties.allowedMethods.split(",").toTypedArray())
            .allowCredentials(true)
    }
}
