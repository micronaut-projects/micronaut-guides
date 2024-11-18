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

import io.micronaut.http.HttpRequest
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import jakarta.inject.Inject

@MicronautTest // <1>
class InfoTest {
    @Inject
    @field:Client("/")
    lateinit var client : HttpClient // <2>

    @Test
    fun testGitComitInfoAppearsInJson() {
        val request: HttpRequest<*> = HttpRequest.GET<Any>("/info") // <3>
        val rsp = client.toBlocking().exchange(request, Map::class.java)
        assertEquals(200, rsp.status().code)
        val info = rsp.body() // <4>
        assertNotNull(info)
        assertNotNull(info.get("git"))
    }
}