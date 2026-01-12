/*
 * Copyright 2017-2025 original authors
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

import com.github.tomakehurst.wiremock.WireMockServer;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.test.support.TestPropertyProvider;

import java.util.Map;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

public abstract class WireMockTestPropertyProvider
        implements TestPropertyProvider { // <1>

    private WireMockServer wireMockServer;

    protected WireMockServer getWireMockServer() {
        if (wireMockServer == null) {
            wireMockServer = new WireMockServer(wireMockConfig().dynamicPort());
            wireMockServer.start();
        }
        return wireMockServer;
    }

    void stopWireMock() {
        if (wireMockServer != null && wireMockServer.isRunning()) {
            wireMockServer.stop();
        }
    }
}
