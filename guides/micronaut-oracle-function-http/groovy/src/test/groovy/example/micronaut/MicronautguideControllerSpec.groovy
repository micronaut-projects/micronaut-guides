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

import com.oracle.bmc.core.model.Instance.LifecycleState
import example.micronaut.mock.MockData
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.oraclecloud.function.http.test.FnHttpTest
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import spock.lang.Specification

import static com.oracle.bmc.core.model.Instance.LifecycleState.Running
import static com.oracle.bmc.core.model.Instance.LifecycleState.Stopped
import static io.micronaut.http.HttpStatus.OK

@MicronautTest
class MicronautguideControllerSpec extends Specification {

    private static final List<Class<?>> SHARED_CLASSES = [MockData, LifecycleState] // <1>

    void 'test status'() {
        given:
        String instanceOcid = UUID.randomUUID()
        MockData.instanceOcid = instanceOcid // <2>
        MockData.instanceLifecycleState = Running

        when:
        HttpResponse<String> response = FnHttpTest.invoke( // <3>
                HttpRequest.GET("/compute/status/$instanceOcid"),
                SHARED_CLASSES)

        then:
        response.status() == OK
        response.body() == '{"lifecycleState":"Running","ocid":"' + instanceOcid + '"}' // <4>
    }

    void 'test start'() {
        given:
        String instanceOcid = UUID.randomUUID()
        MockData.instanceOcid = instanceOcid
        MockData.instanceLifecycleState = Stopped

        when:
        HttpResponse<String> response = FnHttpTest.invoke(
                HttpRequest.POST("/compute/start/$instanceOcid", null),
                SHARED_CLASSES)

        then:
        response.status() == OK
        response.body() == '{"lifecycleState":"Starting","ocid":"' + instanceOcid + '"}'
    }

    void 'test stop'() {
        given:
        String instanceOcid = UUID.randomUUID()
        MockData.instanceOcid = instanceOcid
        MockData.instanceLifecycleState = Running

        when:
        HttpResponse<String> response = FnHttpTest.invoke(
                HttpRequest.POST("/compute/stop/$instanceOcid", null),
                SHARED_CLASSES)

        then:
        response.status() == OK
        response.body() == '{"lifecycleState":"Stopping","ocid":"' + instanceOcid + '"}'
    }

    void cleanup() {
        MockData.reset()
    }
}
