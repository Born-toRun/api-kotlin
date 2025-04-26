package kr.kro.btr.config.hazelcast

import com.hazelcast.core.EntryEvent
import com.hazelcast.core.EntryListener
import com.hazelcast.map.AbstractIMapEvent
import com.hazelcast.map.MapEvent
import com.hazelcast.map.listener.MapClearedListener
import com.hazelcast.map.listener.MapEvictedListener
import io.github.oshai.kotlinlogging.KotlinLogging

class HazelcastMapEventLogger : EntryListener<Any, Any>, MapClearedListener, MapEvictedListener {
    companion object {
        private val log = KotlinLogging.logger {}
    }

    override fun entryAdded(event: EntryEvent<Any, Any>) {
        log(event)
    }

    override fun entryEvicted(event: EntryEvent<Any, Any>) {
        log(event)
    }

    override fun entryRemoved(event: EntryEvent<Any, Any>) {
        log(event)
    }

    override fun entryUpdated(event: EntryEvent<Any, Any>) {
        log(event)
    }

    override fun entryExpired(event: EntryEvent<Any, Any>) {
        log(event)
    }

    override fun mapCleared(event: MapEvent) {
        log(event)
    }

    override fun mapEvicted(event: MapEvent) {
        log(event)
    }

    private fun log(event: AbstractIMapEvent) {
        log.debug {event.toString()}
    }
}