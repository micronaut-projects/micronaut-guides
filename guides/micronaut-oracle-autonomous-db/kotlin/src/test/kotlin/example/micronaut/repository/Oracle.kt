/*
 * Copyright 2017-2023 original authors
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
package example.micronaut.repository

import org.testcontainers.containers.OracleContainer

object Oracle {
    var oracle = OracleContainer("gvenzl/oracle-xe:21-slim-faststart")
        .withDatabaseName("testDB")
        .withUsername("testUser")
        .withPassword("testPassword")

    val configuration: Map<String, Any>
        get() {
            start()
            return java.util.Map.of<String, Any>(
                "datasources.default.url", oracle.jdbcUrl,
                "datasources.default.username", oracle.username,
                "datasources.default.password", oracle.password,
                "datasources.default.driver-class-name", oracle.driverClassName
            )
        }

    fun start() {
        if (!oracle.isRunning()) {
            oracle.start()
        }
    }

    fun stop() {
        if (oracle.isRunning()) {
            oracle.stop()
        }
    }

    fun close() {
        oracle.close()
    }
}
