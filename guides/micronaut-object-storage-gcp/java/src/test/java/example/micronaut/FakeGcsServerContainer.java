/*
 * Copyright 2017-2026 original authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package example.micronaut;

import org.testcontainers.DockerClientFactory;
import org.testcontainers.containers.GenericContainer;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.ServerSocket;

class FakeGcsServerContainer extends GenericContainer<FakeGcsServerContainer> {

    private static final String DEFAULT_IMAGE_NAME = "fsouza/fake-gcs-server:1.40.1";
    private static final int DEFAULT_PORT = 4443;
    private final int hostPort;
    private final String externalUrl;

    public FakeGcsServerContainer() {
        super(DEFAULT_IMAGE_NAME);
        hostPort = findAvailablePort();
        externalUrl = String.format("http://%s:%s", DockerClientFactory.instance().dockerHostIpAddress(), hostPort);
        addFixedExposedPort(hostPort, DEFAULT_PORT);
        this.withCreateContainerCmdModifier(cmd -> cmd.withEntrypoint(
                "/bin/fake-gcs-server",
                "-scheme", "http",
                "-external-url", externalUrl));
    }

    public String getUrl() {
        if (!isRunning()) {
            start();
        }
        return externalUrl;
    }

    private static int findAvailablePort() {
        try (ServerSocket socket = new ServerSocket(0)) {
            return socket.getLocalPort();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
