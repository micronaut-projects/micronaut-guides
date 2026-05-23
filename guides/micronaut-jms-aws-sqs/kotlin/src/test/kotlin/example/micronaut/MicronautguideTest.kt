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

import io.floci.testcontainers.FlociContainer
import io.micronaut.core.annotation.NonNull
import io.micronaut.http.HttpRequest
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import io.micronaut.test.support.TestPropertyProvider
import jakarta.inject.Inject
import org.awaitility.Awaitility
import org.hamcrest.Matchers
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.testcontainers.utility.DockerImageName

@MicronautTest // <1>
@TestInstance(TestInstance.Lifecycle.PER_CLASS) // <2>
internal class MicronautguideTest : TestPropertyProvider { // <3>

    @Inject
    @field:Client("/")
    lateinit var httpClient : HttpClient

    @Inject
    lateinit var demoConsumer: DemoConsumer

    override fun getProperties(): @NonNull MutableMap<String, String> {
        if (!floci.isRunning) {
            floci.start()
        }
        return mapOf(
            "aws.access-key-id" to floci.accessKey,
            "aws.secret-key" to floci.secretKey,
            "aws.region" to floci.region,
            "aws.services.sqs.endpoint-override" to floci.endpoint
        ).toMutableMap()
    }

    @Test
    fun testItWorks() {
        Assertions.assertEquals(0, demoConsumer.getMessageCount())
        httpClient.toBlocking().exchange<Map<Any, Any>, Any>(HttpRequest.POST("/demo", emptyMap()))
        Awaitility.await().until({ demoConsumer.getMessageCount() }, Matchers.equalTo(1))
        Assertions.assertEquals(1, demoConsumer.getMessageCount())
    }

    companion object {
        private val floci: FlociContainer = FlociContainer(DockerImageName.parse("floci/floci:1.5.18"))
    }
}
