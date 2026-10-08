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
package example.micronaut

import org.testcontainers.oracle.OracleContainer
import org.testcontainers.utility.DockerImageName
import org.testcontainers.utility.MountableFile

object Oracle {

    const val IMAGE_NAME = "gvenzl/oracle-free:23.26.1-faststart" // <1>

    // The faststart image already has a database, so startup scripts are the
    // reliable hook for schema creation.
    private const val INIT_SCRIPT = "/container-entrypoint-startdb.d/01-hr-schema.sql"

    private val container: OracleContainer by lazy {
        OracleContainer(DockerImageName.parse(IMAGE_NAME))
            .withCopyFileToContainer(MountableFile.forClasspathResource("oracle-init.sql"), INIT_SCRIPT) // <2>
            .also { oracle ->
                oracle.start() // <3>
                Runtime.getRuntime().addShutdownHook(Thread(oracle::stop))
            }
    }

    fun getProperties(): Map<String, String> = mapOf( // <4>
        "datasources.default.url" to container.jdbcUrl,
        "datasources.default.username" to container.username,
        "datasources.default.password" to container.password
    )
}
