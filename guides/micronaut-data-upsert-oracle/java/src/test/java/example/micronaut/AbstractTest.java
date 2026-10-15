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

import io.micronaut.core.annotation.NonNull;
import io.micronaut.test.support.TestPropertyProvider;
import org.testcontainers.oracle.OracleContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.utility.DockerImageName;

import java.util.Map;

public abstract class AbstractTest implements TestPropertyProvider { // <1>
    public static final String IMAGE_NAME = "gvenzl/oracle-free:23.26.1-faststart";

    @Container // <2>
    static OracleContainer container = new OracleContainer(DockerImageName.parse(IMAGE_NAME));

    @Override
    public @NonNull Map<String, String> getProperties() {
        if (!container.isRunning()) {
            container.start(); // <3>
        }
        return Map.of( // <4>
                "datasources.default.driver-class-name", "oracle.jdbc.OracleDriver",
                "datasources.default.url", container.getJdbcUrl(),
                "datasources.default.username", container.getUsername(),
                "datasources.default.password", container.getPassword());
    }
}
