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

import groovy.transform.CompileStatic
import org.testcontainers.oracle.OracleContainer
import org.testcontainers.utility.DockerImageName
import org.testcontainers.utility.MountableFile

@CompileStatic
final class Oracle {

    public static final String IMAGE_NAME = 'gvenzl/oracle-free:23.26.1-faststart' // <1>

    // The faststart image already has a database, so startup scripts are the
    // reliable hook for schema creation.
    private static final String INIT_SCRIPT = '/container-entrypoint-startdb.d/01-hr-schema.sql'

    private static volatile OracleContainer container

    private Oracle() {
    }

    static Map<String, String> getProperties() {
        OracleContainer current = container
        if (current == null) {
            synchronized (Oracle) {
                current = container
                if (current == null) {
                    current = new OracleContainer(DockerImageName.parse(IMAGE_NAME))
                            .withCopyFileToContainer(MountableFile.forClasspathResource('oracle-init.sql'), INIT_SCRIPT) // <2>
                    current.start() // <3>
                    Runtime.runtime.addShutdownHook(new Thread(current.&stop))
                    container = current
                }
            }
        }
        [ // <4>
                'datasources.default.url'     : current.jdbcUrl,
                'datasources.default.username': current.username,
                'datasources.default.password': current.password,
        ]
    }
}
