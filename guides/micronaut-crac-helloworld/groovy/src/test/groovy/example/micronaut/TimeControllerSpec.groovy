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
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.BlockingHttpClient
import io.micronaut.runtime.server.EmbeddedServer
import spock.lang.Specification

class TimeControllerSpec extends Specification {

    void 'emulate checkpoint'() {
        given:
        EmbeddedServer server = ApplicationContext.run(EmbeddedServer)

        when:
        CheckpointSimulator checkpointSimulator =
            server.applicationContext.getBean(CheckpointSimulator)
        Object time = CheckpointTestUtils.testApp(server, this.&testTime)

        then:
        time == CheckpointTestUtils.testApp(server, this.&testTime)

        when:
        checkpointSimulator.runBeforeCheckpoint()
        server.stop()
        checkpointSimulator.runAfterRestore()
        server.start()

        then:
        time != CheckpointTestUtils.testApp(server, this.&testTime)

        cleanup:
        server.close()
    }

    private String testTime(BlockingHttpClient client) {
        HttpResponse<Map<String, String>> response =
            client.exchange(HttpRequest.GET('/time'), Argument.mapOf(String, String))
        assert response.status == HttpStatus.OK
        assert response.body.isPresent()

        Map<String, String> body = response.body.get()
        assert body.keySet().size() == 1
        assert body.containsKey('time')
        body.time
    }
}
