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

import io.micronaut.context.ApplicationContext
import io.micronaut.core.type.Argument
import io.micronaut.crac.test.CheckpointSimulator
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.BlockingHttpClient
import io.micronaut.runtime.server.EmbeddedServer
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class TimeControllerTest {

    @Test
    fun emulateCheckpoint() {
        ApplicationContext.run(EmbeddedServer::class.java).use { server ->
            val checkpointSimulator = server.applicationContext.getBean(CheckpointSimulator::class.java)
            val time = CheckpointTestUtils.testApp(server, ::testTime)
            assertEquals(time, CheckpointTestUtils.testApp(server, ::testTime))
            checkpointSimulator.runBeforeCheckpoint()
            server.stop()
            checkpointSimulator.runAfterRestore()
            server.start()
            assertNotEquals(time, CheckpointTestUtils.testApp(server, ::testTime))
        }
    }

    private fun testTime(client: BlockingHttpClient): String {
        val response = client.exchange(
            HttpRequest.GET<Any>("/time"),
            Argument.mapOf(String::class.java, String::class.java)
        )
        assertEquals(HttpStatus.OK, response.status)
        val bodyOptional = response.body
        assertTrue(bodyOptional.isPresent)
        val body = bodyOptional.get()
        assertEquals(1, body.keys.size)
        assertTrue(body.containsKey("time"))
        return body["time"]!!
    }
}
