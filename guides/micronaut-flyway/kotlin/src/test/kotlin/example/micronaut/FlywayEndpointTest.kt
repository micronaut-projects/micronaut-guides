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

import io.micronaut.core.type.Argument
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus.OK
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test

@MicronautTest // <1>
class FlywayEndpointTest(@Client("/") val httpClient: HttpClient) { // <2>

    @Test
    fun migrationsAreExposedViaAndEndpoint() {
        val client = httpClient.toBlocking()

        val response = client.exchange(HttpRequest.GET<Any>("/flyway"), Argument.listOf(FlywayReport::class.java))
        assertEquals(OK, response.status())

        val flywayReports = response.body()
        assertNotNull(flywayReports)
        assertEquals(1, flywayReports!!.size)

        val flywayReport = flywayReports[0]
        assertNotNull(flywayReport)
        assertNotNull(flywayReport.migrations)
        assertEquals(2, flywayReport.migrations!!.size)
    }

    internal class FlywayReport {
        var migrations: List<Migration>? = null
    }

    internal class Migration {
        var script: String? = null
            private set

        fun setId(script: String?) {
            this.script = script
        }
    }
}
