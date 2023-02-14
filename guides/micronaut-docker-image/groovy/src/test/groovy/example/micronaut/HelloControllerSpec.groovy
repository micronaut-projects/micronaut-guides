package example.micronaut

import io.micronaut.http.HttpRequest
import io.micronaut.http.client.HttpClient
import io.micronaut.runtime.server.EmbeddedServer
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import spock.lang.Specification
import io.micronaut.context.BeanContext

@MicronautTest
class HelloControllerSpec extends Specification {

    @Inject
    BeanContext beanContext

    void "apex returns JSON"() {
        given:
        HttpClient httpClient = createHttpClient(beanContext)

        when:
        String body = httpClient.toBlocking().retrieve(HttpRequest.GET("/"))
        then:
        body
        "{\"message\":\"Hello World\"}" == body
    }

    private static HttpClient createHttpClient(BeanContext beanContext) {
        beanContext.createBean(HttpClient, "http://localhost:" + beanContext.getBean(EmbeddedServer).getPort())
    }
}