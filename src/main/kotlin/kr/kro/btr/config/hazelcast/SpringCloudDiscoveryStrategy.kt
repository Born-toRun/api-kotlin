package kr.kro.btr.config.hazelcast

import com.hazelcast.cluster.Address
import com.hazelcast.logging.ILogger
import com.hazelcast.spi.discovery.AbstractDiscoveryStrategy
import com.hazelcast.spi.discovery.DiscoveryNode
import com.hazelcast.spi.discovery.SimpleDiscoveryNode
import org.springframework.cloud.client.discovery.DiscoveryClient
import java.net.UnknownHostException

class SpringCloudDiscoveryStrategy(
    private val discoveryClient: DiscoveryClient,
    logger: ILogger,
    properties: Map<String, Comparable<*>>
) : AbstractDiscoveryStrategy(logger, properties) {

    private val serviceId: String = requireNotNull(getOrNull(SpringCloudDiscoveryProperties.SERVICE_ID)) as String
    private val port: Int = requireNotNull(getOrNull(SpringCloudDiscoveryProperties.PORT)) as Int

    override fun discoverNodes(): Iterable<DiscoveryNode> {
        val nodes = mutableListOf<DiscoveryNode>()
        for (instance in discoveryClient.getInstances(serviceId)) {
            try {
                nodes.add(SimpleDiscoveryNode(Address(instance.host, port)))
            } catch (e: UnknownHostException) {
                logger.finest(e.message)
            }
        }
        return nodes
    }
}
