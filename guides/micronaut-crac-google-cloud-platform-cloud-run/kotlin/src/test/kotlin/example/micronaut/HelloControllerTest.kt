package example.micronaut

import io.micronaut.http.HttpRequest
import io.micronaut.http.client.HttpClient
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import io.micronaut.context.BeanContext
import io.micronaut.runtime.server.EmbeddedServer
import jakarta.inject.Inject

@MicronautTest // <1>
class HelloControllerTest {

    @Inject
    lateinit var beanContext: BeanContext

    @Test
    fun testHello() {
        val httpClient = createHttpClient(beanContext) // <2>
        val request: HttpRequest<Any> = HttpRequest.GET("/")  // <3>
        val body = httpClient.toBlocking().retrieve(request)
        assertNotNull(body)
        assertEquals("{\"message\":\"Hello World\"}", body)
    }

    private fun createHttpClient(beanContext: BeanContext): HttpClient {
        val url = "http://localhost:" + beanContext.getBean(EmbeddedServer::class.java).port
        return beanContext.createBean(HttpClient::class.java, url)
    }
}