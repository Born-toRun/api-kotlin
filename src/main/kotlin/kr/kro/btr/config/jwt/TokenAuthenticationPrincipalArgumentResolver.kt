package kr.kro.btr.config.jwt

import kr.kro.btr.support.TokenDetail
import org.springframework.core.MethodParameter
import org.springframework.core.convert.converter.Converter
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import org.springframework.stereotype.Component
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer

@Component
class TokenAuthenticationPrincipalArgumentResolver(
    private val customJwtAuthenticationConverter: Converter<JwtAuthenticationToken, TokenDetail>
) : HandlerMethodArgumentResolver {

    override fun supportsParameter(parameter: MethodParameter): Boolean {
        return parameter.hasParameterAnnotation(AuthenticationPrincipal::class.java) &&
                parameter.parameterType == TokenDetail::class.java
    }

    override fun resolveArgument(
        parameter: MethodParameter,
        mavContainer: ModelAndViewContainer?,
        webRequest: NativeWebRequest,
        binderFactory: WebDataBinderFactory?
    ): Any? {
        return try {
            val authentication = SecurityContextHolder.getContext().authentication as JwtAuthenticationToken
            customJwtAuthenticationConverter.convert(authentication)
        } catch (e: Exception) {
            null
        }
    }
}
