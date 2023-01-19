package example.micronaut

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
class LiquibaseEndpointTest(@Client("/") val client: HttpClient) { // <2>

    @Test
    fun migrationsAreExposedViaAndEndpoint() {
        val client = httpClient.toBlocking()

        val response = client.exchange(HttpRequest.GET<Any>("/liquibase"), LiquibaseReport::class.java)
        assertEquals(OK, response.status())

        val liquibaseReport = response.body()
        assertNotNull(liquibaseReport)
        assertNotNull(liquibaseReport!!.changeSets)
        assertEquals(2, liquibaseReport.changeSets!!.size)
    }

    internal class LiquibaseReport {
        var changeSets: List<ChangeSet>? = null
    }

    internal class ChangeSet {
        var id: String? = null
    }
}
