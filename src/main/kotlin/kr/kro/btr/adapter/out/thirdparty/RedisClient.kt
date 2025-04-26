package kr.kro.btr.adapter.out.thirdparty

import org.redisson.api.RedissonClient
import org.springframework.stereotype.Component
import java.time.Duration

@Component
class RedisClient(private val redissonClient: RedissonClient) {

    fun save(key: String, value: Any) {
        redissonClient.getBucket<Any>(key).set(value)
    }

    fun save(key: String, value: Any, duration: Duration) {
        redissonClient.getBucket<Any>(key).set(value, duration)
    }

    fun remove(key: String) {
        redissonClient.getBucket<Any>(key).delete()
    }

    fun exist(key: String): Boolean {
        return redissonClient.getBucket<Any>(key).isExists
    }

    fun get(key: String): Any? {
        return redissonClient.getBucket<Any>(key).get()
    }

    fun getAndRemove(key: String): Any? {
        return redissonClient.getBucket<Any>(key).getAndDelete()
    }

    fun add(key: String, value: String) {
        redissonClient.getList<String>(key).add(value)
    }

    fun removeAll(key: String) {
        redissonClient.getList<String>(key).clear()
    }

    fun removeValue(key: String, value: String) {
        redissonClient.getList<String>(key).remove(value)
    }

    fun getList(key: String): List<Any> {
        return redissonClient.getList<Any>(key).readAll()
    }
}
