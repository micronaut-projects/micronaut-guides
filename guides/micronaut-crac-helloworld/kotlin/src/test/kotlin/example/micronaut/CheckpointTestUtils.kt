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
import io.micronaut.crac.test.CheckpointSimulator
import io.micronaut.http.client.BlockingHttpClient
import io.micronaut.http.client.HttpClient
import io.micronaut.runtime.server.EmbeddedServer

object CheckpointTestUtils {

    fun test(testScenario: (BlockingHttpClient) -> Unit) {
        ApplicationContext.run(EmbeddedServer::class.java).use { server ->
            val checkpointSimulator = server.applicationContext.getBean(CheckpointSimulator::class.java)
            testApp(server, testScenario)

            checkpointSimulator.runBeforeCheckpoint()
            server.stop()
            checkpointSimulator.runAfterRestore()
            server.start()
            testApp(server, testScenario)
        }
    }

    fun <T> testApp(embeddedServer: EmbeddedServer, clientConsumer: (BlockingHttpClient) -> T): T =
        embeddedServer.applicationContext.createBean(HttpClient::class.java, embeddedServer.url).use { httpClient ->
            clientConsumer(httpClient.toBlocking())
        }
}
