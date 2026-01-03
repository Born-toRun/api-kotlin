package kr.kro.btr.config

import com.hazelcast.config.*
import com.hazelcast.spi.properties.ClusterProperty
import kr.kro.btr.config.hazelcast.HazelcastMapEventLogger
import kr.kro.btr.config.hazelcast.Kryo5Serializer
import kr.kro.btr.config.hazelcast.SpringCloudDiscoveryStrategyFactory
import org.springframework.beans.factory.ObjectProvider
import org.springframework.beans.factory.annotation.Value
import org.springframework.cloud.client.discovery.DiscoveryClient
import org.springframework.cloud.client.serviceregistry.Registration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
class HazelcastConfig(
    private val discoveryClient: DiscoveryClient?,
    registration: ObjectProvider<Registration?>
) {
    private val registration: Registration? = registration.getIfAvailable()

    @Value("\${spring.cache.port}")
    private val port = 0

    @Bean
    fun config(): Config {
        val config = Config()
        config.instanceName = "borntorun"
        configureNetwork(config.networkConfig)

        configureCacheConfig(config.getMapConfig("user"))
        configureCacheConfig(config.getMapConfig("crew"))
        configureCacheConfig(config.getMapConfig("feed"))
        configureCacheConfig(config.getMapConfig("activity"))
        configureCacheConfig(config.getMapConfig("comment"))
        configureCacheConfig(config.getMapConfig("default"))

        configureSerialization(config.serializationConfig)
        return config
    }

    private fun configureNetwork(networkConfig: NetworkConfig) {
        val joinConfig: JoinConfig = networkConfig.join
        networkConfig.port = port
        if (registration == null) {
            System.setProperty("hazelcast.local.localAddress", "127.0.0.1")
        } else {
            joinConfig.multicastConfig.isEnabled = false
            configureDiscovery(joinConfig.discoveryConfig, registration.serviceId, port)
        }
    }

    private fun configureDiscovery(discoveryConfig: DiscoveryConfig, serviceId: String?, port: Int) {
        System.setProperty(ClusterProperty.DISCOVERY_SPI_ENABLED.name, "true")
        System.setProperty(ClusterProperty.WAIT_SECONDS_BEFORE_JOIN.name, "1")
        System.setProperty(ClusterProperty.MERGE_FIRST_RUN_DELAY_SECONDS.name, "10")

        val properties = java.util.HashMap<String?, String?>()
        properties.put("serviceId", serviceId)
        properties.put("port", port.toString())
        discoveryConfig.addDiscoveryStrategyConfig(
            DiscoveryStrategyConfig()
                .setDiscoveryStrategyFactory(SpringCloudDiscoveryStrategyFactory(discoveryClient!!))
                .setProperties(java.util.Collections.unmodifiableMap<String?, Comparable<*>?>(properties))
        )
    }

    private fun configureCacheConfig(mapConfig: MapConfig) {
        mapConfig
            .setTimeToLiveSeconds(300)
            .setMaxIdleSeconds(60)
            .setEvictionConfig(
                EvictionConfig()
                    .setEvictionPolicy(EvictionPolicy.LRU)
                    .setMaxSizePolicy(MaxSizePolicy.USED_HEAP_PERCENTAGE)
                    .setSize(20)
            )
            .addEntryListenerConfig(
                EntryListenerConfig()
                    .setImplementation(HazelcastMapEventLogger())
                    .setLocal(false)
                    .setIncludeValue(false)
            )
            .setBackupCount(1)
            .setAsyncBackupCount(0)
            .setStatisticsEnabled(true)
    }

    private fun configureSerialization(serializationConfig: SerializationConfig) {
        serializationConfig.globalSerializerConfig = GlobalSerializerConfig()
            .setImplementation(Kryo5Serializer())
            .setOverrideJavaSerialization(true)
    }
}
