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

import io.micronaut.core.annotation.NonNull
import io.micronaut.http.HttpRequest
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import io.micronaut.test.support.TestPropertyProvider
import jakarta.inject.Inject
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.testcontainers.junit.jupiter.Testcontainers
import java.util.*

@Testcontainers(disabledWithoutDocker = true)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@MicronautTest
internal class MicronautguideTest : TestPropertyProvider {

    @Inject
    @field:Client("/")
    lateinit var client : HttpClient

    @Inject
    lateinit var demoConsumer: DemoConsumer

    @Test
    fun testItWorks() {
        var messageCount = demoConsumer.getMessageCount()
        Assertions.assertTrue(messageCount == 0)

        client.toBlocking().exchange<Any, Any>(HttpRequest.POST("/demo", emptyMap<Any, Any>()))
        messageCount = demoConsumer.getMessageCount()
        while (messageCount == 0) {
            messageCount = demoConsumer.getMessageCount()
        }
        Assertions.assertTrue(messageCount == 1)
    }

    @NonNull
    override fun getProperties(): Map<String, String> {
        return LocalStackUtil.properties
    }

    @AfterAll
    fun afterAll() {
        LocalStackUtil.close()
    }

}