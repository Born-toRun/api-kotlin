package kr.kro.btr.support.oauth.info.impl

import kr.kro.btr.support.oauth.info.OAuth2UserInfo

class FacebookOAuth2UserInfo(attributes: Map<String, Any>) : OAuth2UserInfo(attributes) {

    override fun getId(): String {
        return attributes["id"] as? String ?: ""
    }

    override fun getName(): String {
        return attributes["name"] as? String ?: ""
    }

    override fun getEmail(): String {
        return attributes["email"] as? String ?: ""
    }

    override fun getImageUrl(): String {
        return attributes["imageUrl"] as? String ?: ""
    }
}