package example.micronaut
/*
//tag::package[]
package example.micronaut
//tag::package[]
*/
//tag::imports[]

import io.micronaut.core.type.Argument
import io.micronaut.http.HttpRequest
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import java.util.UUID

@MicronautTest // <1>
class RequestScopeTest(@Client("/") val httpClient: HttpClient) { // <2>
// end::imports[]

    // tag::test[]
    @ParameterizedTest
    @ValueSource(strings = ["/request"])
    fun requestScopeScopeIsACustomScopeThatIndicatesANewInstanceOfTheBeanIsCreatedAndAssociatedWithEachHTTPRequest(path: String) {
        val responses = executeRequest(httpClient, createRequest(path)).toMutableSet()
        assertEquals(1, responses.size) // <3>
        responses.addAll(executeRequest(httpClient, createRequest(path)))
        assertEquals(2, responses.size) // <4>
    }

    private fun executeRequest(client: HttpClient, request: HttpRequest<Any>): List<String> {
        return client.toBlocking().retrieve(
            request,
            Argument.listOf(String::class.java)
        )
    }

    private fun createRequest(path: String): HttpRequest<Any> {
        return HttpRequest.GET<Any>(path).header("UUID", UUID.randomUUID().toString())
    }
}
//end::test[]