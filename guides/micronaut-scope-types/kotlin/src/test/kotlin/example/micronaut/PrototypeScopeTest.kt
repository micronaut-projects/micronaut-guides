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
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

@MicronautTest // <1>
class PrototypeScopeTest(@Client("/") val httpClient: HttpClient) { // <2>
//end::imports[]

    //tag::testheader[]
    @ParameterizedTest
    @ValueSource(strings = ["/prototype"])
    //end::testheader[]

    //tag::test[]
    fun prototypeScopeIndicatesThatANewInstanceOfTheBeanIsCreatedEachTimeItIsInjected(path: String) {
        val responses = executeRequest(httpClient, path).toMutableSet()
        assertEquals(2, responses.size) // <3>
        responses.addAll(executeRequest(httpClient, path))
        assertEquals(2, responses.size) // <4>
    }

    private fun executeRequest(client: HttpClient, path: String): List<String> {
        return client.toBlocking().retrieve(
            HttpRequest.GET<Any>(path),
            Argument.listOf(String::class.java)
        )
    }
    //end::test[]

}