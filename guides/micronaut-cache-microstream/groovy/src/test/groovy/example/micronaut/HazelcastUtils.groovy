package example.micronaut

import org.testcontainers.containers.GenericContainer
import org.testcontainers.utility.DockerImageName

class HazelcastUtils {
    static GenericContainer container

    static void start() {
        start("hazelcast/hazelcast:4.2.1", 5701)
    }

    static void start(String fullImageName, Integer... ports) {
        if (container == null) {
            container = new GenericContainer(DockerImageName.parse(fullImageName))
                    .withExposedPorts(ports)
        }
        if (!container.isRunning()) {
            container.start()
        }
    }

    static String getUrl() {
        if (container == null || !container.isRunning()) {
            start()
        }
        return container.getContainerIpAddress() + ":" + container.getFirstMappedPort()
    }

    static void close() {
        if (container != null) {
            container.close()
        }
    }
}