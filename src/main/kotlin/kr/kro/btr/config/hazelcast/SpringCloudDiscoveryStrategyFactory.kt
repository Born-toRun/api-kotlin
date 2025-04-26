package kr.kro.btr.config.hazelcast

import com.hazelcast.config.properties.PropertyDefinition
import com.hazelcast.logging.ILogger
import com.hazelcast.spi.discovery.DiscoveryNode
import com.hazelcast.spi.discovery.DiscoveryStrategy
import com.hazelcast.spi.discovery.DiscoveryStrategyFactory
import org.springframework.cloud.client.discovery.DiscoveryClient

class SpringCloudDiscoveryStrategyFactory(private val discoveryClient: DiscoveryClient): DiscoveryStrategyFactory {

    companion object {
        private val PROPERTY_DEFINITIONS: Collection<PropertyDefinition>

        init {
            val propertyDefinitions = mutableListOf<PropertyDefinition>()
            propertyDefinitions.add(SpringCloudDiscoveryProperties.SERVICE_ID)
            propertyDefinitions.add(SpringCloudDiscoveryProperties.PORT)
            PROPERTY_DEFINITIONS = propertyDefinitions.toList()
        }
    }

    override fun getDiscoveryStrategyType(): Class<out DiscoveryStrategy> {
        return SpringCloudDiscoveryStrategy::class.java
    }

    override fun newDiscoveryStrategy(
        discoveryNode: DiscoveryNode,
        logger: ILogger,
        properties: Map<String, Comparable<*>>
    ): DiscoveryStrategy {
        return SpringCloudDiscoveryStrategy(discoveryClient, logger, properties)
    }

    override fun getConfigurationProperties(): Collection<PropertyDefinition> {
        return PROPERTY_DEFINITIONS
    }
}
