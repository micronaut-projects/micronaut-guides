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

import com.oracle.bmc.core.model.Instance.LifecycleState
import com.oracle.bmc.core.model.Instance.LifecycleState.Running
import com.oracle.bmc.core.model.Instance.LifecycleState.Stopped
import example.micronaut.mock.MockData
import example.micronaut.mock.MockData.reset
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus.OK
import io.micronaut.oraclecloud.function.http.test.FnHttpTest
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import java.util.UUID

@MicronautTest
@Disabled("Kotlin tests currently fail due to issue with Fn")
class MicronautguideControllerTest {

    @Test
    fun testStatus() {
        val instanceOcid = UUID.randomUUID().toString()
        MockData.instanceOcid = instanceOcid; // <2>
        MockData.instanceLifecycleState = Running;

        val response = FnHttpTest.invoke( // <3>
                HttpRequest.GET<Any>("/compute/status/$instanceOcid"),
                SHARED_CLASSES)
        assertEquals(OK, response.status())
        assertEquals(
                "{\"lifecycleState\":\"Running\",\"ocid\":\"$instanceOcid\"}", // <4>
                response.body())
    }

    @Test
    fun testStart() {
        val instanceOcid = UUID.randomUUID().toString()
        MockData.instanceOcid = instanceOcid;
        MockData.instanceLifecycleState = Stopped;

        val response = FnHttpTest.invoke(
                HttpRequest.POST<Any?>("/compute/start/$instanceOcid", null),
                SHARED_CLASSES)

        assertEquals(OK, response.status())
        assertEquals(
                "{\"lifecycleState\":\"Starting\",\"ocid\":\"$instanceOcid\"}",
                response.body())
    }

    @Test
    fun testStop() {
        val instanceOcid = UUID.randomUUID().toString()
        MockData.instanceOcid = instanceOcid;
        MockData.instanceLifecycleState = Running;

        val response = FnHttpTest.invoke(
                HttpRequest.POST<Any?>("/compute/stop/$instanceOcid", null),
                SHARED_CLASSES)

        assertEquals(OK, response.status())
        assertEquals(
                "{\"lifecycleState\":\"Stopping\",\"ocid\":\"$instanceOcid\"}",
                response.body())
    }

    @AfterEach
    fun cleanup() {
        reset()
    }

    companion object {
        private val SHARED_CLASSES = listOf( // <1>
                MockData::class.java,
                LifecycleState::class.java)
    }
}
