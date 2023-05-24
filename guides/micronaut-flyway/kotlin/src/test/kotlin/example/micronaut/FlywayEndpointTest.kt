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
