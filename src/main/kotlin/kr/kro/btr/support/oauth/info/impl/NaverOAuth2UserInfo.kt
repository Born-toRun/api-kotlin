package kr.kro.btr.support.oauth.info.impl

import kr.kro.btr.support.oauth.info.OAuth2UserInfo

class NaverOAuth2UserInfo(attributes: Map<String, Any>) : OAuth2UserInfo(attributes) {

    private fun getResponse(): Map<*, *> {
        return attributes["response"] as Map<*, *>
    }

    override fun getId(): String {
        val response = getResponse()
        return response["id"] as String
    }

    override fun getName(): String {
        val response = getResponse()
        return response["nickname"] as String
    }

    override fun getEmail(): String {
        val response = getResponse()
        return response["email"] as String
    }

    override fun getImageUrl(): String {
        val response = getResponse()
        return response["profile_image"] as String
    }
}