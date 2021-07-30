package example.micronaut

import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.Assert
import org.junit.jupiter.api.Test
import javax.inject.Inject

@MicronautTest
class StaticResourceTest {
    @Inject
    @field:Client("/")
    lateinit var client: HttpClient

    @Test
    fun staticResourcesAreExposedAtPublic() {
        val response: HttpResponse<*> = client.toBlocking().exchange<Any>("/index.html")
        Assert.assertEquals(HttpStatus.OK, response.status())
    }
}