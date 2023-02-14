package example.micronaut

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
    lateinit var beanContext: BeanContext // <2>

    @Test
    fun testHello() {
        val httpClient = createHttpClient(beanContext)
        val body = httpClient.toBlocking().retrieve("/")
        assertNotNull(body)
        assertEquals("{\"message\":\"Hello World\"}", body)
    }

    private fun createHttpClient(beanContext: BeanContext): HttpClient {
        return beanContext.createBean(
            HttpClient::class.java, "http://localhost:" + beanContext.getBean(EmbeddedServer::class.java).port
        )
    }
}