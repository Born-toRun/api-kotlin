package kr.kro.btr.utils

import org.springframework.test.web.servlet.ResultActions
import org.springframework.test.web.servlet.ResultMatcher
import org.springframework.test.web.servlet.result.JsonPathResultMatchers

fun ResultActions.andExpectData(
	vararg matchers: ResultMatcher
): ResultActions {
	return andExpectAll(*matchers)
}

infix fun JsonPathResultMatchers.shouldBe(
	expectedValue: Any?
): ResultMatcher {
	return this.value(expectedValue.toString())
}
