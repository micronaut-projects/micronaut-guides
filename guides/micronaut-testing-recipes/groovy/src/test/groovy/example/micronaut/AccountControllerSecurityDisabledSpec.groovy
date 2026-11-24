package example.micronaut

import io.micronaut.context.annotation.Property
import io.micronaut.core.util.StringUtils
import io.micronaut.http.HttpRequest
import io.micronaut.http.MediaType
import io.micronaut.http.client.BlockingHttpClient
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import spock.lang.Specification

@Property(name = "micronaut.security.filter.enabled", value = StringUtils.FALSE) // <1>
@MicronautTest // <2>
class AccountControllerSecurityDisabledSpec extends Specification {

    @Inject
    @Client("/")
    HttpClient httpClient // <3>

    void "account is accessible with security disabled"() {
        given:
        BlockingHttpClient client = httpClient.toBlocking()
        HttpRequest<?> request = HttpRequest.GET("/account").accept(MediaType.TEXT_PLAIN)

        when:
        String body = client.retrieve(request) // <4>

        then:
        noExceptionThrown()
        body == "Your account"
    }
}
