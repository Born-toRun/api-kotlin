package kr.kro.btr.config.hazelcast

import com.esotericsoftware.kryo.Kryo
import com.esotericsoftware.kryo.KryoException
import com.esotericsoftware.kryo.Serializer
import com.esotericsoftware.kryo.io.Input
import com.esotericsoftware.kryo.io.Output
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.data.domain.PageImpl

class PageSerializer : Serializer<PageImpl<*>>() {

    private val objectMapper = ObjectMapper()

    override fun write(kryo: Kryo, output: Output, page: PageImpl<*>) {
        try {
            val json = objectMapper.writeValueAsString(page)
            output.writeString(json)
        } catch (e: JsonProcessingException) {
            throw KryoException("cannot serialize PageImpl!")
        }
    }

    override fun read(kryo: Kryo, input: Input, type: Class<out PageImpl<*>>): PageImpl<*> {
        val json = input.readString()
        return try {
            objectMapper.readValue(json, object : TypeReference<PageImpl<*>>() {})
        } catch (ex: JsonProcessingException) {
            throw KryoException("cannot deserialize PageImpl!")
        }
    }
}
