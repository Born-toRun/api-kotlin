package kr.kro.btr.config.hazelcast

import com.hazelcast.config.properties.PropertyDefinition
import com.hazelcast.config.properties.PropertyTypeConverter
import com.hazelcast.config.properties.SimplePropertyDefinition
import com.hazelcast.config.properties.ValueValidator

class SpringCloudDiscoveryProperties private constructor() {

    companion object {
        val SERVICE_ID: PropertyDefinition = property("serviceId", PropertyTypeConverter.STRING)
        val PORT: PropertyDefinition = property("port", PropertyTypeConverter.INTEGER)

        private fun property(
            key: String,
            typeConverter: PropertyTypeConverter,
            valueValidator: ValueValidator<Comparable<Any>>? = null
        ): PropertyDefinition {
            return SimplePropertyDefinition(key, true, typeConverter, valueValidator)
        }
    }
}
