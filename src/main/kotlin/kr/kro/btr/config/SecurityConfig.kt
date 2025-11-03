package kr.kro.btr.config

import kr.kro.btr.config.properties.AppProperties
import kr.kro.btr.domain.constant.RoleType
import kr.kro.btr.domain.port.UserPort
import kr.kro.btr.domain.port.UserRefreshTokenPort
import kr.kro.btr.support.http.model.CustomErrorAttributes
import kr.kro.btr.support.oauth.RestAuthenticationEntryPoint
import kr.kro.btr.support.oauth.filter.TokenAuthenticationFilter
import kr.kro.btr.support.oauth.handler.OAuth2AuthenticationFailureHandler
import kr.kro.btr.support.oauth.handler.OAuth2AuthenticationSuccessHandler
import kr.kro.btr.support.oauth.handler.TokenAccessDeniedHandler
import kr.kro.btr.support.oauth.repository.OAuth2AuthorizationRequestBasedOnCookieRepository
import kr.kro.btr.support.oauth.service.BornToRunOAuth2UserService
import kr.kro.btr.support.oauth.service.BornToRunUserDetailsService
import kr.kro.btr.support.oauth.token.AuthTokenProvider
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.web.servlet.error.ErrorAttributes
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
@EnableWebSecurity
@EnableConfigurationProperties(AppProperties::class)
class SecurityConfig(
    private val tokenProvider: AuthTokenProvider,
    private val appProperties: AppProperties,
    private val userRefreshTokenPort: UserRefreshTokenPort,
    private val userPort: UserPort,
){

    @Bean
    fun filterChain(
        httpSecurity: HttpSecurity,
        oAuth2UserService: BornToRunOAuth2UserService,
        tokenAccessDeniedHandler: TokenAccessDeniedHandler,
        userDetailsService: BornToRunUserDetailsService,
        tokenProvider: AuthTokenProvider
    ): SecurityFilterChain {
        val usersBased = "/api/v1/users"
        val feedsBased = "/api/v1/feeds"
        val crewsBased = "/api/v1/crews"
        val marathonBookmarkBased = "/api/v1/marathons/bookmark"
        val commentBased = "/api/v1/comments"
        val objectStorageBased = "/api/v1/object-storage"
        val recommendationBased = "/api/v1/recommendations"
        val activityBased = "/api/v1/activities"
        val recentSearchKeywordBased = "/api/v1/recent-search-keywords"
        val privacyBased = "/api/v1/privacy"
        val yellowCardBased = "/api/v1/yellow-cards"
        val announceBased = "/api/v1/announces"
        val feedbackBased = "/api/v1/feedbacks"

        return httpSecurity
            .headers { headers ->
                headers.xssProtection { it.disable() }
            }
            .httpBasic { it.disable() }
            .csrf { it.disable() }
            .cors { }
            .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
            .exceptionHandling { exceptionHandling ->
                exceptionHandling
                    .authenticationEntryPoint(RestAuthenticationEntryPoint())
                    .accessDeniedHandler(tokenAccessDeniedHandler)
            }
            .addFilterBefore(
                tokenAuthenticationFilter(tokenProvider),
                UsernamePasswordAuthenticationFilter::class.java
            )
            .authorizeHttpRequests { auth ->
                auth.requestMatchers(HttpMethod.POST, "/api/v1/users/refresh").permitAll()
                    .requestMatchers(HttpMethod.POST,
                    announceBased)
                    .hasAnyAuthority(RoleType.ADMIN.code)

                    .requestMatchers(HttpMethod.PUT,
                        announceBased)
                    .hasAnyAuthority(RoleType.ADMIN.code)

                    .requestMatchers(HttpMethod.DELETE,
                        announceBased)
                    .hasAnyAuthority(RoleType.ADMIN.code)

                    .requestMatchers(HttpMethod.POST,
                        "/api/v1/auth/sign-out",
                        "$commentBased/{feedId}",
                        "$objectStorageBased/{bucket}",
                        "$recommendationBased/{recommendationType}/{contentId}",
                        "$activityBased/**",
                        "$marathonBookmarkBased/{marathonId}",
                        yellowCardBased,
                        feedsBased,
                        feedbackBased)
                    .authenticated()
                    .requestMatchers(HttpMethod.GET,
                        "$privacyBased/users",
                        "$usersBased/my",
                        "$activityBased/**",
                        "$announceBased/**")
                    .authenticated()
                    .requestMatchers(HttpMethod.PUT,
                        "$usersBased/sign-up",
                        usersBased,
                        "$feedsBased/{feedId}",
                        "$activityBased/**",
                        "$privacyBased/users",
                        "$commentBased/{commentId}",)
                    .authenticated()
                    .requestMatchers(HttpMethod.DELETE,
                        usersBased,
                        "$feedsBased/{feedId}",
                        "$commentBased/{commentId}",
                        "$objectStorageBased/{bucket}/{fileId}",
                        "$recommendationBased/{recommendationType}/{contentId}",
                        "$activityBased/**",
                        recentSearchKeywordBased,
                        "$recentSearchKeywordBased/{keyword}",
                        "$marathonBookmarkBased/{marathonId}")
                    .authenticated()
                    .anyRequest().permitAll()
            }
            .userDetailsService(userDetailsService)
            .oauth2Login { oauth2 ->
                oauth2.authorizationEndpoint { it.baseUri("/oauth2/authorization").authorizationRequestRepository(oAuth2AuthorizationRequestBasedOnCookieRepository()) }
                    .redirectionEndpoint { it.baseUri("/*/oauth2/code/*") }
                    .userInfoEndpoint { it.userService(oAuth2UserService) }
                    .successHandler(oAuth2AuthenticationSuccessHandler())
                    .failureHandler(oAuth2AuthenticationFailureHandler())
            }
            .build()
    }

    @Bean
    fun passwordEncoder(): BCryptPasswordEncoder = BCryptPasswordEncoder()

    fun tokenAuthenticationFilter(tokenProvider: AuthTokenProvider): TokenAuthenticationFilter {
        return TokenAuthenticationFilter(tokenProvider)
    }

    @Bean("OAuth2AuthorizationRequestBasedOnCookieRepository")
    fun oAuth2AuthorizationRequestBasedOnCookieRepository(): OAuth2AuthorizationRequestBasedOnCookieRepository {
        return OAuth2AuthorizationRequestBasedOnCookieRepository()
    }

    @Bean
    fun oAuth2AuthenticationSuccessHandler(): OAuth2AuthenticationSuccessHandler {
        return OAuth2AuthenticationSuccessHandler(
            tokenProvider,
            appProperties,
            userPort,
            userRefreshTokenPort,
            oAuth2AuthorizationRequestBasedOnCookieRepository()
        )
    }

    @Bean
    fun oAuth2AuthenticationFailureHandler(): OAuth2AuthenticationFailureHandler {
        return OAuth2AuthenticationFailureHandler(oAuth2AuthorizationRequestBasedOnCookieRepository())
    }

    @Bean
    fun errorAttributes(): ErrorAttributes {
        return CustomErrorAttributes()
    }
}
