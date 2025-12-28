package kr.kro.btr.adapter.`in`.web

import kr.kro.btr.adapter.out.persistence.ActivityRepository
import kr.kro.btr.adapter.out.thirdparty.RedisClient
import kr.kro.btr.core.service.ActivityService
import kr.kro.btr.domain.entity.ActivityEntity
import kr.kro.btr.infrastructure.ActivityGateway
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.cache.CacheManager
import org.springframework.test.context.ActiveProfiles
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

/**
 * 활동 상세 조회 시 출석 코드가 포함되는지 검증하는 통합 테스트
 *
 * 검증 사항:
 * 1. open() 호출 전: Redis에 출석 코드 없음
 * 2. open() 호출 후: Redis에 출석 코드 생성됨
 * 3. searchWithAggregation() 호출: 출석 코드가 포함된 ActivityAggregationData 반환
 */
@SpringBootTest
@ActiveProfiles("test")
@Transactional
class ActivityDetailAttendanceCodeIntegrationTest {

    @Autowired
    private lateinit var activityRepository: ActivityRepository

    @Autowired
    private lateinit var activityGateway: ActivityGateway

    @Autowired
    private lateinit var activityService: ActivityService

    @Autowired
    private lateinit var redisClient: RedisClient

    @Autowired
    private lateinit var cacheManager: CacheManager

    private lateinit var testActivity: ActivityEntity
    private val testUserId = 1L

    @BeforeEach
    fun setUp() {
        // 캐시 초기화
        cacheManager.cacheNames.forEach { cacheName ->
            cacheManager.getCache(cacheName)?.clear()
        }

        // 테스트 활동 생성 (시작 시간이 현재로부터 5분 후)
        testActivity = ActivityEntity(
            title = "테스트 활동",
            contents = "테스트 내용",
            startAt = LocalDateTime.now().plusMinutes(5),
            venue = "테스트 장소",
            venueUrl = "https://example.com",
            participantsLimit = 10,
            participationFee = 0,
            userId = testUserId
        )
        testActivity = activityRepository.save(testActivity)
    }

    @AfterEach
    fun tearDown() {
        // Redis 정리
        val accessCodeKey = "accessCode${testActivity.id}"
        if (redisClient.exist(accessCodeKey)) {
            redisClient.remove(accessCodeKey)
        }

        // 캐시 초기화
        cacheManager.cacheNames.forEach { cacheName ->
            cacheManager.getCache(cacheName)?.clear()
        }
    }

    @Test
    fun `open 전에는 Redis에 출석 코드가 없어야 한다`() {
        // Given
        val accessCodeKey = "accessCode${testActivity.id}"

        // When
        val exists = redisClient.exist(accessCodeKey)

        // Then
        assertFalse(exists, "open 전에는 Redis에 출석 코드가 없어야 합니다")

        println("=== Before Open ===")
        println("Key: $accessCodeKey")
        println("Exists: $exists")
    }

    @Test
    fun `open 후 Redis에 출석 코드가 생성되어야 한다`() {
        // Given
        val accessCodeKey = "accessCode${testActivity.id}"

        // When: 활동 오픈
        activityGateway.open(testActivity.id, testUserId)

        // Then: Redis에 출석 코드가 생성됨
        val exists = redisClient.exist(accessCodeKey)
        val accessCode = if (exists) redisClient.get(accessCodeKey) else null
        val ttl = if (exists) redisClient.ttl(accessCodeKey) else -1

        println("=== After Open - Redis Status ===")
        println("Key: $accessCodeKey")
        println("Exists: $exists")
        println("Access Code: $accessCode")
        println("TTL: $ttl seconds")

        assertTrue(exists, "Redis에 출석 코드가 존재해야 합니다")
        assertNotNull(accessCode, "출석 코드가 null이 아니어야 합니다")
        assertTrue(ttl > 0, "TTL이 0보다 커야 합니다. 실제값: $ttl")
        assertTrue(ttl <= 300, "TTL이 300초(5분) 이하여야 합니다. 실제값: $ttl")

        val codeValue = accessCode as Int
        assertTrue(codeValue >= 1000 && codeValue <= 9999,
            "출석 코드는 1000-9999 범위여야 합니다. 실제값: $codeValue")
    }

    @Test
    fun `getAccessCode는 Redis에서 출석 코드를 정확히 가져와야 한다`() {
        // Given: 활동 오픈하여 출석 코드 생성
        activityGateway.open(testActivity.id, testUserId)

        // When: getAccessCode 호출
        val result = activityGateway.getAccessCode(testActivity.id)

        // Then
        assertNotNull(result, "getAccessCode 결과가 null이 아니어야 합니다")

        val (accessCode, expiresAt) = result!!
        println("=== getAccessCode Result ===")
        println("Access Code: $accessCode")
        println("Expires At: $expiresAt")

        assertTrue(accessCode >= 1000 && accessCode <= 9999,
            "출석 코드는 1000-9999 범위여야 합니다. 실제값: $accessCode")
        assertTrue(expiresAt.isAfter(LocalDateTime.now()),
            "만료 시간은 미래여야 합니다. 실제값: $expiresAt")
    }

    @Test
    fun `searchWithAggregation은 출석 코드를 포함해야 한다`() {
        // Given: 활동 오픈하여 출석 코드 생성
        activityGateway.open(testActivity.id, testUserId)

        // When: searchWithAggregation 호출
        val aggregation = activityGateway.searchWithAggregation(testActivity.id, testUserId)

        // Then
        println("=== searchWithAggregation Result ===")
        println("Activity ID: ${aggregation.activityEntity.id}")
        println("Attendance Code: ${aggregation.attendanceCode}")
        println("Attendance Expires At: ${aggregation.attendanceExpiresAt}")

        assertNotNull(aggregation.attendanceCode, "출석 코드가 null이 아니어야 합니다")
        assertNotNull(aggregation.attendanceExpiresAt, "만료 시간이 null이 아니어야 합니다")

        val code = aggregation.attendanceCode!!
        assertTrue(code >= 1000 && code <= 9999,
            "출석 코드는 1000-9999 범위여야 합니다. 실제값: $code")

        val expiresAt = aggregation.attendanceExpiresAt!!
        assertTrue(expiresAt.isAfter(LocalDateTime.now()),
            "만료 시간은 미래여야 합니다. 실제값: $expiresAt")
    }

    @Test
    fun `ActivityService search는 출석 코드를 포함한 ActivityResult를 반환해야 한다`() {
        // Given: 활동 오픈하여 출석 코드 생성
        activityGateway.open(testActivity.id, testUserId)

        // 캐시 무효화
        cacheManager.getCache("activity")?.clear()

        // When: ActivityService.search 호출
        val result = activityService.search(testActivity.id, testUserId)

        // Then
        println("=== ActivityService.search Result ===")
        println("Activity ID: ${result.id}")
        println("Attendance Code: ${result.attendanceCode}")
        println("Expires At: ${result.expiresAt}")

        assertNotEquals(0, result.attendanceCode, "출석 코드가 0이 아니어야 합니다")
        assertNotNull(result.expiresAt, "만료 시간이 null이 아니어야 합니다")

        assertTrue(result.attendanceCode >= 1000 && result.attendanceCode <= 9999,
            "출석 코드는 1000-9999 범위여야 합니다. 실제값: ${result.attendanceCode}")

        assertTrue(result.expiresAt!!.isAfter(LocalDateTime.now()),
            "만료 시간은 미래여야 합니다. 실제값: ${result.expiresAt}")
    }

    @Test
    fun `여러 번 조회해도 동일한 출석 코드를 반환해야 한다`() {
        // Given: 활동 오픈
        activityGateway.open(testActivity.id, testUserId)

        // When: 여러 번 조회
        val codes = mutableListOf<Int>()
        repeat(5) {
            // 캐시 무효화
            cacheManager.getCache("activity")?.clear()

            val result = activityService.search(testActivity.id, testUserId)
            codes.add(result.attendanceCode)
        }

        // Then: 모든 출석 코드가 동일해야 함
        println("=== Multiple Search Results ===")
        println("Codes: $codes")

        assertTrue(codes.all { it != 0 }, "모든 출석 코드가 0이 아니어야 합니다")
        assertEquals(1, codes.distinct().size,
            "모든 출석 코드가 동일해야 합니다. 실제값: $codes")
    }
}
