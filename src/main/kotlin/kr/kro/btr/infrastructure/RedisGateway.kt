package kr.kro.btr.infrastructure

import kr.kro.btr.adapter.out.thirdparty.RedisClient
import org.springframework.stereotype.Component
import java.time.Duration

@Component
class RedisGateway(
    private val redisClient: RedisClient
) {

    fun save(key: String, value: Any) {
        redisClient.save(key, value)
    }

    fun save(key: String, value: Any, duration: Duration) {
        redisClient.save(key, value, duration)
    }

    fun remove(key: String) {
        redisClient.remove(key)
    }

    fun exist(key: String): Boolean {
        return redisClient.exist(key)
    }

    fun get(key: String): Any? {
        return redisClient.get(key)
    }

    fun getAndRemove(key: String): Any? {
        return redisClient.getAndRemove(key)
    }

    fun add(key: String, value: String) {
        redisClient.add(key, value)
    }

    fun removeAll(key: String) {
        redisClient.removeAll(key)
    }

    fun removeValue(key: String, value: String) {
        redisClient.removeValue(key, value)
    }

    fun getList(key: String): List<Any> {
        return redisClient.getList(key)
    }
}
