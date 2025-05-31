package kr.kro.btr.utils.restdocs

import org.springframework.restdocs.request.ParameterDescriptor
import org.springframework.restdocs.request.PathParametersSnippet
import org.springframework.restdocs.request.QueryParametersSnippet
import org.springframework.restdocs.request.RequestDocumentation

class RestDocsParam(
	val descriptor: ParameterDescriptor
) {

    private var default: String
        get() = descriptor.attributes["default"] as String
        set(value) {
            descriptor.attributes["default"] = value
        }

    infix fun pathMeans(description: String): RestDocsParam {
        descriptor.description(description)
        return this
    }
}

infix fun String.isRequired(isRequired: Boolean): RestDocsParam {
    return createField(this, isRequired)
}

private fun createField(
	value: String,
	optional: Boolean = false
): RestDocsParam {
	val descriptor = RequestDocumentation
		.parameterWithName(value)

	if (optional) descriptor.optional()

	return RestDocsParam(descriptor)
}

fun pathParameters(vararg params: RestDocsParam): PathParametersSnippet {
	return RequestDocumentation.pathParameters(params.map { it.descriptor })
}

fun queryParameters(vararg params: RestDocsParam): QueryParametersSnippet {
	return RequestDocumentation.queryParameters(params.map { it.descriptor })
}
