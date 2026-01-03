package kr.kro.btr.config.hazelcast

import com.esotericsoftware.kryo.Kryo
import com.esotericsoftware.kryo.io.InputChunked
import com.esotericsoftware.kryo.io.OutputChunked
import com.esotericsoftware.kryo.serializers.CompatibleFieldSerializer
import com.esotericsoftware.kryo.serializers.EnumNameSerializer
import com.esotericsoftware.kryo.util.DefaultClassResolver
import com.esotericsoftware.kryo.util.DefaultInstantiatorStrategy
import com.esotericsoftware.kryo.util.Pool
import com.hazelcast.nio.ObjectDataInput
import com.hazelcast.nio.ObjectDataOutput
import com.hazelcast.nio.serialization.StreamSerializer
import org.objenesis.strategy.StdInstantiatorStrategy
import org.slf4j.LoggerFactory
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import java.io.InputStream
import java.io.OutputStream

class Kryo5Serializer : StreamSerializer<Any> {

    override fun write(out: ObjectDataOutput, obj: Any) {
        val kryo = pool.obtain()
        try {
            val output = OutputChunked(out as OutputStream)
            kryo.writeClassAndObject(output, obj)
            output.endChunk()
            output.flush()
        } catch (e: Exception) {
            logger.error("Kryo serialization error for object type: ${obj.javaClass.name}", e)
            throw e
        } finally {
            pool.free(kryo)
        }
    }

    override fun read(`in`: ObjectDataInput): Any {
        val kryo = pool.obtain()
        try {
            val input = InputChunked(`in` as InputStream)
            val obj = kryo.readClassAndObject(input)
            return obj
        } catch (e: Exception) {
            logger.error("Kryo deserialization error", e)
            throw e
        } finally {
            pool.free(kryo)
        }
    }

    override fun getTypeId(): Int = 1

    override fun destroy() {
        pool.clean()
    }

    private class KryoPool : Pool<Kryo>(true, true) {
        private val classResolver = ClassResolver()

        override fun create(): Kryo {
            return Kryo(classResolver, null).apply {
                references = true

                // Spring Data 관련 클래스 등록
                register(PageImpl::class.java)
                register(Sort::class.java)
                register(Pageable::class.java)

                // CompatibleFieldSerializer 설정 - 필드 변경에 대한 호환성 제공
                val compatibleFieldSerializer = CompatibleFieldSerializer::class.java
                setDefaultSerializer(compatibleFieldSerializer)

                // Enum은 EnumNameSerializer로 직렬화
                addDefaultSerializer(Enum::class.java, EnumNameSerializer::class.java)

                // Objenesis로 생성자 없이 객체 생성 가능
                instantiatorStrategy = DefaultInstantiatorStrategy(StdInstantiatorStrategy())

                // 등록되지 않은 클래스도 직렬화 허용
                isRegistrationRequired = false

                // 클래스 구조 변경 시 더 관대하게(?)
                setWarnUnregisteredClasses(true)
                setOptimizedGenerics(false)
            }
        }
    }

    private class ClassResolver : DefaultClassResolver() {
        override fun getTypeByName(className: String): Class<*>? {
            return super.getTypeByName(className) ?: try {
                val contextClassLoader = Thread.currentThread().contextClassLoader
                try {
                    Class.forName(className, false, contextClassLoader)
                } catch (e: ClassNotFoundException) {
                    Class.forName(className, false, ClassLoader.getSystemClassLoader())
                }
            } catch (ex: ClassNotFoundException) {
                logger.warn("Class not found during deserialization: $className", ex)
                null
            }
        }
    }

    companion object {
        private val logger = LoggerFactory.getLogger(Kryo5Serializer::class.java)
        private val pool = KryoPool()
    }
}
