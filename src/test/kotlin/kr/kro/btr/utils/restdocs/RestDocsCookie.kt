package kr.kro.btr.utils.restdocs

import org.springframework.restdocs.cookies.CookieDescriptor
import org.springframework.restdocs.cookies.CookieDocumentation
import org.springframework.restdocs.cookies.RequestCookiesSnippet

class RestDocsCookie(
	val descriptor: CookieDescriptor
)

infix fun String.cookieMeans(
	description: String
): RestDocsCookie {
	return createField(this, description)
}

private fun createField(
	value: String,
	description: String,
	optional: Boolean = false
): RestDocsCookie {
	val descriptor = CookieDocumentation
		.cookieWithName(value)
		.description(description)

	if (optional) descriptor.optional()

	return RestDocsCookie(descriptor)
}

fun requestCookies(vararg params: RestDocsCookie): RequestCookiesSnippet {
	return CookieDocumentation.requestCookies(params.map { it.descriptor })
}
