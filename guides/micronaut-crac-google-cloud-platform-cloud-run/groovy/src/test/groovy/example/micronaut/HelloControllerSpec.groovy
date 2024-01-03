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
package example.micronaut

import io.micronaut.context.ApplicationContext
import io.micronaut.crac.test.CheckpointSimulator
import io.micronaut.http.client.BlockingHttpClient
import io.micronaut.http.client.HttpClient
import io.micronaut.runtime.server.EmbeddedServer
import spock.lang.Specification

class HelloControllerSpec extends Specification {

    void "test hello endpoint works after checkpoint"() throws IOException {
        given:
        EmbeddedServer server = ApplicationContext.run(EmbeddedServer)
        BlockingHttpClient client = createHttpClient(server)

        when:
        String body = client.retrieve("/")

        then:
        body == '{"message":"Hello World"}'

        when:
        CheckpointSimulator checkpointSimulator = server.applicationContext.getBean(CheckpointSimulator)
        checkpointSimulator.runBeforeCheckpoint()

        then:
        !server.running

        when:
        client.close()
        checkpointSimulator.runAfterRestore()

        then:
        server.running

        when:
        client = createHttpClient(server)

        body = client.retrieve("/")

        then:
        body == '{"message":"Hello World"}'

        cleanup:
        client.close()
    }

    private static BlockingHttpClient createHttpClient(EmbeddedServer embeddedServer) {
        String url = "http://localhost:${embeddedServer.port}"
        return embeddedServer.applicationContext.createBean(HttpClient, url).toBlocking();
    }
}