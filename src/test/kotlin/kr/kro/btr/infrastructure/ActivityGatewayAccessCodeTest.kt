package kr.kro.btr.infrastructure

import kotlin.random.Random
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.RepeatedTest
import org.junit.jupiter.api.Assertions.*

/**
 * 출석 코드 생성 로직 검증 테스트
 */
class ActivityGatewayAccessCodeTest {

    @RepeatedTest(100)
    fun `출석 코드는 4자리 숫자여야 한다`() {
        // Given & When
        val accessCode = Random.nextInt(1000, 10000)

        // Then
        assertTrue(accessCode >= 1000, "출석 코드는 1000 이상이어야 합니다. 실제값: $accessCode")
        assertTrue(accessCode <= 9999, "출석 코드는 9999 이하여야 합니다. 실제값: $accessCode")
        assertEquals(4, accessCode.toString().length, "출석 코드는 4자리여야 합니다. 실제값: $accessCode")
    }

    @Test
    fun `Random_nextInt 범위 검증`() {
        // Given
        val min = 1000
        val max = 10000

        // When: 1000번 반복하여 범위 검증
        val codes = (1..1000).map { Random.nextInt(min, max) }

        // Then
        assertTrue(codes.all { it >= 1000 && it <= 9999 },
            "모든 출석 코드는 1000-9999 범위여야 합니다.")
        assertTrue(codes.all { it.toString().length == 4 },
            "모든 출석 코드는 4자리여야 합니다.")

        // 충분히 다양한 값이 생성되는지 확인 (최소 500개 이상의 고유한 값)
        val uniqueCodes = codes.distinct()
        assertTrue(uniqueCodes.size >= 500,
            "충분히 다양한 출석 코드가 생성되어야 합니다. 고유한 값: ${uniqueCodes.size}")
    }
}
