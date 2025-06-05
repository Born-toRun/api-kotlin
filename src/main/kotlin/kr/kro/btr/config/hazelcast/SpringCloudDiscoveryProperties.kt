package kr.kro.btr.config.hazelcast

import com.hazelcast.config.properties.PropertyDefinition
import com.hazelcast.config.properties.PropertyTypeConverter
import com.hazelcast.config.properties.SimplePropertyDefinition
import com.hazelcast.config.properties.ValueValidator

class SpringCloudDiscoveryProperties private constructor() {

    companion object {
        val SERVICE_ID = createProperty("serviceId", PropertyTypeConverter.STRING)
        val PORT = createProperty("port", PropertyTypeConverter.INTEGER)

        private fun createProperty(
            key: String,
            typeConverter: PropertyTypeConverter,
            valueValidator: ValueValidator<Comparable<Any>>? = null
        ): PropertyDefinition {
            return SimplePropertyDefinition(key, true, typeConverter, valueValidator)
        }
    }
}
