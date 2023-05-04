package example.micronaut;

import org.testcontainers.containers.GenericContainer;

class FakeGcsServerContainer extends GenericContainer<FakeGcsServerContainer> {

    private static final String DEFAULT_IMAGE_NAME = "fsouza/fake-gcs-server:1.40.1";
    private static final int DEFAULT_PORT = 4443;

    public FakeGcsServerContainer() {
        super(DEFAULT_IMAGE_NAME);
        this.withExposedPorts(DEFAULT_PORT)
                .withCreateContainerCmdModifier(cmd -> cmd.withEntrypoint("/bin/fake-gcs-server", "-scheme", "http"));
    }

    public String getUrl() {
        if (!isRunning()) {
            start();
        }
        return String.format("http://%s:%s", getHost(), getMappedPort(DEFAULT_PORT));
    }
}
