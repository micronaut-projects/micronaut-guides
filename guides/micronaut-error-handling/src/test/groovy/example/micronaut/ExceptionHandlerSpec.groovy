package example.micronaut

import io.micronaut.context.ApplicationContext
import io.micronaut.http.HttpRequest
import io.micronaut.http.client.BlockingHttpClient
import io.micronaut.http.client.HttpClient
import io.micronaut.runtime.server.EmbeddedServer
import spock.lang.AutoCleanup
import spock.lang.Shared
import spock.lang.Specification

class ExceptionHandlerSpec extends Specification {
    @Shared
    @AutoCleanup
    EmbeddedServer embeddedServer = ApplicationContext.run(EmbeddedServer)

    @Shared
    BlockingHttpClient client = embeddedServer.applicationContext.createBean(HttpClient, embeddedServer.URL).toBlocking()

    void "test OutOfStockException is handled by ExceptionHandler"() {
        when:
        HttpRequest request = HttpRequest.GET('/books/stock/1234')
        Integer stock = client.retrieve(request, Integer)

        then:
        noExceptionThrown()
        stock != null
        stock == 0
    }
}
