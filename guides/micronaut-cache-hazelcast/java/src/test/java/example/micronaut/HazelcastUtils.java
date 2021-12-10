package example.micronaut;

import org.testcontainers.containers.GenericContainer;
import org.testcontainers.utility.DockerImageName;

public class HazelcastUtils {
    static GenericContainer container;

    public static void start() {
        start("hazelcast/hazelcast:4.2.1", 5701);
    }
    public static void start(String fullImageName, Integer... ports) {
        if (container == null) {
            container = new GenericContainer(DockerImageName.parse(fullImageName))
                    .withExposedPorts(ports);
        }
        if (!container.isRunning()) {
            container.start();
        }
    }

    public static String getUrl() {
        if (container == null || !container.isRunning()) {
            start();
        }
        return container.getContainerIpAddress() + ":" + container.getFirstMappedPort();
    }

    public static void close() {
        if (container != null) {
            container.close();
        }
    }
}