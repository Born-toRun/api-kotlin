package kr.kro.btr.support.http.model

import org.springframework.boot.web.error.ErrorAttributeOptions
import org.springframework.boot.web.servlet.error.ErrorAttributes
import org.springframework.web.context.request.WebRequest

class CustomErrorAttributes : ErrorAttributes {
    override fun getErrorAttributes(
        webRequest: WebRequest,
        options: ErrorAttributeOptions
    ): Map<String, Any> {
        val errorAttributes = mutableMapOf<String, Any>()

        val message = webRequest.getAttribute("jakarta.servlet.error.message", WebRequest.SCOPE_REQUEST) as Any
        errorAttributes["message"] = message

        return errorAttributes
    }

    override fun getError(webRequest: WebRequest): Throwable? {
        return webRequest.getAttribute("jakarta.servlet.error.exception", WebRequest.SCOPE_REQUEST) as? Throwable
    }
}
