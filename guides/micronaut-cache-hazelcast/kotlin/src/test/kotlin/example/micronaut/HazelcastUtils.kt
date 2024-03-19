package example.micronaut

import org.testcontainers.containers.GenericContainer
import org.testcontainers.utility.DockerImageName

object HazelcastUtils {
    var container: GenericContainer<*>? = null

    private fun start() {
        start("hazelcast/hazelcast:4.2.1", 5701)
    }

    private fun start(fullImageName: String, port: Int) {
        if (container == null) {
            this.container = GenericContainer(DockerImageName.parse(fullImageName))
                .withExposedPorts(port)
        }
        if (!container!!.isRunning) {
            container?.start()
        }
    }

    fun url(): String {
        if (container == null || !container!!.isRunning) {
            start()
        }
        return container!!.containerIpAddress + ":" + container!!.firstMappedPort
    }

    fun close() {
        container?.close()
    }
}