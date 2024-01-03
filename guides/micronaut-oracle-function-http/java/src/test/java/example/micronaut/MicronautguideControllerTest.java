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
package example.micronaut;

import com.oracle.bmc.core.model.Instance.LifecycleState;
import example.micronaut.mock.MockData;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.oraclecloud.function.http.test.FnHttpTest;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static com.oracle.bmc.core.model.Instance.LifecycleState.Running;
import static com.oracle.bmc.core.model.Instance.LifecycleState.Stopped;
import static io.micronaut.http.HttpStatus.OK;
import static org.junit.jupiter.api.Assertions.assertEquals;

@MicronautTest
class MicronautguideControllerTest {

    private static final List<Class<?>> SHARED_CLASSES = Arrays.asList( // <1>
            MockData.class,
            LifecycleState.class);

    @Test
    void testStatus() {

        String instanceOcid = UUID.randomUUID().toString();
        MockData.instanceOcid = instanceOcid; // <2>
        MockData.instanceLifecycleState = Running;

        HttpResponse<String> response = FnHttpTest.invoke( // <3>
                HttpRequest.GET("/compute/status/" + instanceOcid),
                SHARED_CLASSES);

        assertEquals(OK, response.status());
        assertEquals(
                "{\"lifecycleState\":\"Running\",\"ocid\":\"" + instanceOcid + "\"}", // <4>
                response.body());
    }

    @Test
    void testStart() {

        String instanceOcid = UUID.randomUUID().toString();
        MockData.instanceOcid = instanceOcid;
        MockData.instanceLifecycleState = Stopped;

        HttpResponse<String> response = FnHttpTest.invoke(
                HttpRequest.POST("/compute/start/" + instanceOcid, null),
                SHARED_CLASSES);

        assertEquals(OK, response.status());
        assertEquals(
                "{\"lifecycleState\":\"Starting\",\"ocid\":\"" + instanceOcid + "\"}",
                response.body());
    }

    @Test
    void testStop() {

        String instanceOcid = UUID.randomUUID().toString();
        MockData.instanceOcid = instanceOcid;
        MockData.instanceLifecycleState = Running;

        HttpResponse<String> response = FnHttpTest.invoke(
                HttpRequest.POST("/compute/stop/" + instanceOcid, null),
                SHARED_CLASSES);

        assertEquals(OK, response.status());
        assertEquals(
                "{\"lifecycleState\":\"Stopping\",\"ocid\":\"" + instanceOcid + "\"}",
                response.body());
    }

    @AfterEach
    void cleanup() {
        MockData.reset();
    }
}
