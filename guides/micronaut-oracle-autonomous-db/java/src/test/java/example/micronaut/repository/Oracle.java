/*
 * Copyright 2017-2024 original authors
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
package example.micronaut.repository;

import org.testcontainers.containers.OracleContainer;

import java.util.Map;

public class Oracle {

    public static OracleContainer oracle = new OracleContainer("gvenzl/oracle-xe:21-slim-faststart")
            .withDatabaseName("testDB")
            .withUsername("testUser")
            .withPassword("testPassword");

    public static Map<String, Object> getConfiguration() {
        start();
        return Map.of("datasources.default.url", oracle.getJdbcUrl(),
                "datasources.default.username", oracle.getUsername(),
                "datasources.default.password", oracle.getPassword(),
                "datasources.default.driver-class-name", oracle.getDriverClassName());
    }
    public static void start() {
        if (!oracle.isRunning()) {
            oracle.start();
        }
    }

    public static void stop() {
        if (oracle.isRunning()) {
            oracle.stop();
        }
    }

    public static void close() {
        oracle.close();
    }

}
