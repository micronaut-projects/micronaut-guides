package example.micronaut

import io.micronaut.context.annotation.Property
import io.micronaut.core.util.StringUtils
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.MediaType
import io.micronaut.http.client.BlockingHttpClient
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.cookie.Cookie
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import spock.lang.Specification

@Property(name = "micronaut.http.client.follow-redirects", value = StringUtils.FALSE) // <1>
@MicronautTest // <2>
class LocaleControllerSpec extends Specification {

    @Inject
    @Client("/")
    HttpClient httpClient // <3>

    void "locale is saved in a cookie"() {
        given:
        BlockingHttpClient client = httpClient.toBlocking()
        HttpRequest<?> request = HttpRequest.POST("/locale", [locale: "es"])
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)

        when:
        HttpResponse<?> response = client.exchange(request) // <4>

        then:
        noExceptionThrown()
        response.status() == HttpStatus.SEE_OTHER

        when:
        Optional<Cookie> cookie = response.getCookie("locale") // <5>

        then:
        cookie.present
        cookie.get().value == "es"
    }
}
