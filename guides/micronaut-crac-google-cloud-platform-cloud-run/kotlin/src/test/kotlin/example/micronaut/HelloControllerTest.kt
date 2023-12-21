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
package example.micronaut

import io.micronaut.context.ApplicationContext
import io.micronaut.crac.test.CheckpointSimulator
import io.micronaut.http.client.HttpClient
import org.junit.jupiter.api.Test
import io.micronaut.runtime.server.EmbeddedServer

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue

class HelloControllerTest {

    @Test
    fun testHello() {
        ApplicationContext.run(EmbeddedServer::class.java).use { server ->
            var client = createHttpClient(server)

            var body: String = client.retrieve("/")

            assertNotNull(body)
            assertEquals("{\"message\":\"Hello World\"}", body)

            val checkpointSimulator = server.applicationContext.getBean(CheckpointSimulator::class.java)
            checkpointSimulator.runBeforeCheckpoint()

            assertFalse(server.isRunning)
            client.close()

            checkpointSimulator.runAfterRestore()
            assertTrue(server.isRunning)

            client = createHttpClient(server)

            body = client.retrieve("/")

            assertNotNull(body)
            assertEquals("{\"message\":\"Hello World\"}", body)

            client.close()
        }
    }

    private fun createHttpClient(server: EmbeddedServer) =
        server.applicationContext.createBean(HttpClient::class.java, server.url).toBlocking()
}