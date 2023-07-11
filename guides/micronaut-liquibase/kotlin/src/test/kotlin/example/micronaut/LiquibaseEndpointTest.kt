package example.micronaut

import io.micronaut.core.type.Argument
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus.OK
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.serde.annotation.Serdeable
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test

@MicronautTest // <1>
class LiquibaseEndpointTest(@Client("/") val httpClient: HttpClient) { // <2>

    @Test
    fun migrationsAreExposedViaAndEndpoint() {
        val client = httpClient.toBlocking()

        val response = client.exchange(HttpRequest.GET<Any>("/liquibase"), Argument.listOf(LiquibaseReport::class.java))
        assertEquals(OK, response.status())

        val liquibaseReport = response.body().get(0)
        assertNotNull(liquibaseReport)
        assertNotNull(liquibaseReport!!.changeSets)
        assertEquals(2, liquibaseReport.changeSets!!.size)
    }
}

@Serdeable
class LiquibaseReport {
    var changeSets: List<ChangeSet>? = null
}

@Serdeable
class ChangeSet {
    var id: String? = null
}
