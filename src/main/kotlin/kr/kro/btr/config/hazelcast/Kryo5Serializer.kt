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
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import java.io.InputStream
import java.io.OutputStream

class Kryo5Serializer : StreamSerializer<Any> {

    override fun write(out: ObjectDataOutput, obj: Any) {
        val kryo = pool.obtain()
        val output = OutputChunked(out as OutputStream)
        kryo.writeClassAndObject(output, obj)
        output.endChunk()
        output.flush()
        pool.free(kryo)
    }

    override fun read(`in`: ObjectDataInput): Any {
        val kryo = pool.obtain()
        val input = InputChunked(`in` as InputStream)
        val obj = kryo.readClassAndObject(input)
        pool.free(kryo)
        return obj
    }

    override fun getTypeId(): Int = 1

    override fun destroy() {
        pool.clean()
    }

    companion object {
        private val pool = KryoPool()
    }

    private class KryoPool : Pool<Kryo>(true, true) {
        // TODO: resolver setting
        private val classResolver = ClassResolver()

        override fun create(): Kryo {
            return Kryo(classResolver, null).apply {
                references = true
                register(PageImpl::class.java)
                register(Sort::class.java)
                register(Pageable::class.java)
                setDefaultSerializer(CompatibleFieldSerializer::class.java)
                addDefaultSerializer(Enum::class.java, EnumNameSerializer::class.java)
                instantiatorStrategy = DefaultInstantiatorStrategy(StdInstantiatorStrategy())
                isRegistrationRequired = false
            }
        }
    }

    private class ClassResolver : DefaultClassResolver() {
        override fun getTypeByName(className: String): Class<*>? {
            return super.getTypeByName(className) ?: try {
                Class.forName(className, false, Thread.currentThread().contextClassLoader)
            } catch (ex: ClassNotFoundException) {
                null
            }
        }
    }
}
