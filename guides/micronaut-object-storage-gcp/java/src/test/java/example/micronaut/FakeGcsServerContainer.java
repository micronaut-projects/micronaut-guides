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

import org.testcontainers.containers.GenericContainer;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

class FakeGcsServerContainer extends GenericContainer<FakeGcsServerContainer> {

    private static final String DEFAULT_IMAGE_NAME = "fsouza/fake-gcs-server:1.40.1";
    private static final int DEFAULT_PORT = 4443;
    private boolean externalUrlConfigured;

    public FakeGcsServerContainer() {
        super(DEFAULT_IMAGE_NAME);
        this.withExposedPorts(DEFAULT_PORT)
                .withCreateContainerCmdModifier(cmd -> cmd.withEntrypoint("/bin/fake-gcs-server", "-scheme", "http"));
    }

    @Override
    public void start() {
        super.start();
        configureExternalUrl();
    }

    public String getUrl() {
        if (!isRunning()) {
            start();
        }
        return String.format("http://%s:%s", getHost(), getMappedPort(DEFAULT_PORT));
    }

    private void configureExternalUrl() {
        if (externalUrlConfigured) {
            return;
        }
        String url = getUrl();
        HttpRequest request = HttpRequest.newBuilder(URI.create(url + "/_internal/config"))
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString("{\"externalUrl\":\"" + url + "\"}"))
                .build();
        try {
            HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.discarding());
            externalUrlConfigured = true;
        } catch (IOException e) {
            throw new IllegalStateException("Unable to configure fake-gcs-server external URL", e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("Interrupted while configuring fake-gcs-server external URL", e);
        }
    }
}
