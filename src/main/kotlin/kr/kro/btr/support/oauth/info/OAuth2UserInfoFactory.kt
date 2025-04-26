package kr.kro.btr.support.oauth.info

import kr.kro.btr.domain.constant.ProviderType
import kr.kro.btr.domain.constant.ProviderType.*
import kr.kro.btr.support.oauth.info.impl.FacebookOAuth2UserInfo
import kr.kro.btr.support.oauth.info.impl.GoogleOAuth2UserInfo
import kr.kro.btr.support.oauth.info.impl.KakaoOAuth2UserInfo
import kr.kro.btr.support.oauth.info.impl.NaverOAuth2UserInfo

object OAuth2UserInfoFactory {
    fun getOAuth2UserInfo(providerType: ProviderType, attributes: Map<String, Any>): OAuth2UserInfo {
        return when (providerType) {
            GOOGLE -> GoogleOAuth2UserInfo(attributes)
            FACEBOOK -> FacebookOAuth2UserInfo(attributes)
            NAVER -> NaverOAuth2UserInfo(attributes)
            KAKAO -> KakaoOAuth2UserInfo(attributes)
            else -> throw java.lang.IllegalArgumentException("Invalid Provider Type.")
        }
    }
}
