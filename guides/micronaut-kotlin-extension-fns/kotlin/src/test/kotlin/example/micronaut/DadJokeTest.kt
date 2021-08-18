package example.micronaut
import io.micronaut.kotlin.context.createBean
import io.micronaut.kotlin.context.run
import io.micronaut.http.HttpRequest
import io.micronaut.http.client.HttpClient
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
