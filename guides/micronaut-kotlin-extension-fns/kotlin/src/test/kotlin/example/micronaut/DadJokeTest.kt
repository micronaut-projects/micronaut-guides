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
import io.micronaut.kotlin.context.createBean
import io.micronaut.kotlin.context.run
import io.micronaut.kotlin.http.retrieveList
import io.micronaut.kotlin.http.retrieveObject
import io.micronaut.runtime.server.EmbeddedServer
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Test

class DadJokeTest {

    @Test
    fun testDadJokeController() {
        val embeddedServer = run<EmbeddedServer>() // <1>
        val client = embeddedServer.applicationContext.createBean<HttpClient>(embeddedServer.url).toBlocking() // <2>

        // Test single object retrieve extension
        val anyJoke = client.retrieveObject<String>(HttpRequest.GET("/dadJokes/joke")) // <3>
        assertFalse(anyJoke.isNullOrBlank())

        // Test list retrieve extension
        val dogJoke = client.retrieveList<DadJoke>(HttpRequest.GET("/dadJokes/dogJokes")) // <3>
        assertFalse(dogJoke.isEmpty())
        assertFalse(dogJoke.first().joke.isNullOrBlank())

        client.close()
        embeddedServer.close()
    }
}
