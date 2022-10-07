package example.micronaut;

import io.micronaut.core.io.socket.SocketUtils;
import org.testcontainers.containers.FixedHostPortGenericContainer;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.utility.DockerImageName;

public class FakeGcsServerContainer extends FixedHostPortGenericContainer<FakeGcsServerContainer> {

    public static final String DEFAULT_IMAGE_NAME = "fsouza/fake-gcs-server:1.40.1";
    public static final int DEFAULT_PORT = 4443;

    private int port;

    public FakeGcsServerContainer(String dockerImageName) {
        super(dockerImageName);
    }

    @Override
    protected void configure() {
        super.configure();

        this.port = SocketUtils.findAvailableTcpPort();
        addFixedExposedPort(port, DEFAULT_PORT);

        withCreateContainerCmdModifier(cmd -> cmd.withEntrypoint("/bin/fake-gcs-server", "-scheme", "http"));
    }

    public int getPort() {
        return port;
    }
}
