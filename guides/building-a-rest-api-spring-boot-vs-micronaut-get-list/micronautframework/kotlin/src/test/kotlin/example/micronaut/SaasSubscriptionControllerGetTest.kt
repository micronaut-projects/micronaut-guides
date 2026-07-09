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

import com.jayway.jsonpath.JsonPath
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.test.annotation.Sql
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

@Sql(value = ["classpath:schema.sql", "classpath:data.sql"]) // <1>
@MicronautTest // <2>
class SaasSubscriptionControllerGetTest(
    @param:Client("/") private val httpClient: HttpClient // <3>
) {

    @Test
    fun shouldReturnASaasSubscriptionWhenDataIsSaved() {
        val client = httpClient.toBlocking()
        val response = client.exchange("/subscriptions/99", String::class.java)
        assertThat(response.status.code).isEqualTo(HttpStatus.OK.code)

        val documentContext = JsonPath.parse(response.body())
        val id: Number? = documentContext.read("$.id")
        assertThat(id).isNotNull()
        assertThat(id).isEqualTo(99)

        val name: String? = documentContext.read("$.name")
        assertThat(name).isNotNull()
        assertThat(name).isEqualTo("Advanced")

        val cents: Int? = documentContext.read("$.cents")
        assertThat(cents).isEqualTo(2900)
    }

    @Test
    fun shouldNotReturnASaasSubscriptionWithAnUnknownId() {
        val client = httpClient.toBlocking()
        val thrown = assertThrows<HttpClientResponseException> { // <4>
            client.exchange("/subscriptions/1000", String::class.java)
        }
        assertThat(thrown.status.code).isEqualTo(HttpStatus.NOT_FOUND.code)
        assertThat(thrown.response.getBody(String::class.java)).isEmpty()
    }
}
