package kr.kro.btr.support

import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.util.SerializationUtils
import java.util.*

object CookieSupport {

    fun getCookie(request: HttpServletRequest, name: String): Cookie? {
        val cookies = request.cookies
        return cookies.firstOrNull { it.name == name }
    }

    fun addCookie(request: HttpServletRequest, response: HttpServletResponse, name: String, value: String, maxAge: Int) {
        val cookie = Cookie(name, value).apply {
            path = "/"
            isHttpOnly = true
            this.maxAge = maxAge
        }

        val isSecure = request.getHeader("X-Forwarded-Proto")?.equals("https", ignoreCase = true)
            ?: request.isSecure

        val cookieHeader = buildString {
            append("${cookie.name}=${cookie.value}")
            append("; Path=${cookie.path}")
            append("; Max-Age=${cookie.maxAge}")
            append("; HttpOnly")

            if (isSecure) {
                append("; SameSite=None; Secure")
            } else {
                append("; SameSite=Lax")
            }
        }

        response.addHeader("Set-Cookie", cookieHeader)
    }

    fun deleteCookie(request: HttpServletRequest, response: HttpServletResponse, name: String) {
        val cookies = request.cookies ?: return
        cookies.filter { it.name == name }.forEach {
            it.value = ""
            it.path = "/"
            it.maxAge = 0
            response.addCookie(it)
        }
    }

    fun serialize(obj: Any): String {
        val bytes = SerializationUtils.serialize(obj) ?: throw IllegalArgumentException("Unable to serialize object.")
        return Base64.getUrlEncoder().encodeToString(bytes)
    }

    fun <T : Any> deserialize(cookie: Cookie, klass: Class<T>): T? {
        return try {
            val bytes = Base64.getUrlDecoder().decode(cookie.value)
            val deserialized = SerializationUtils.deserialize(bytes)
            klass.cast(deserialized)
        } catch (ex: Exception) {
            null
        }
    }

}
