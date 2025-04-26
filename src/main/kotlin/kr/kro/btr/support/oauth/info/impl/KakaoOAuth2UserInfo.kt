package kr.kro.btr.support.oauth.info.impl

import kr.kro.btr.support.oauth.info.OAuth2UserInfo


class KakaoOAuth2UserInfo(attributes: Map<String, Any>) : OAuth2UserInfo(attributes) {

    override fun getId(): String {
        return attributes["sub"]?.toString() ?: ""
    }

    override fun getName(): String {
        val properties = attributes["properties"] as Map<*, *>
        return properties["nickname"] as String
    }

    override fun getEmail(): String {
        return attributes["account_email"] as String
    }

    override fun getImageUrl(): String {
        val properties = attributes["properties"] as Map<*, *>
        return properties["thumbnail_image"] as String
    }
}