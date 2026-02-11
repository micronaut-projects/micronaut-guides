package example.micronaut

import io.micronaut.context.annotation.Property
import io.micronaut.context.annotation.Requires
import io.micronaut.core.async.publisher.Publishers
import io.micronaut.http.HttpRequest
import io.micronaut.http.MediaType
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.security.authentication.Authentication
import io.micronaut.security.filters.AuthenticationFetcher
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import jakarta.inject.Singleton
import org.reactivestreams.Publisher
import spock.lang.Specification

@Property(name = "spec.name", value = "PlanControllerAuthenticatedWithRolesSpec") // <1>
@MicronautTest // <2>
class PlanControllerAuthenticatedWithRolesSpec extends Specification {

    @Inject
    @Client("/")
    HttpClient httpClient // <3>

    void "plan controller returns evil plan for authenticated user with evil mastermind role"() {
        when:
        String response = httpClient.toBlocking().retrieve(HttpRequest.GET("/plan").accept(MediaType.TEXT_PLAIN))

        then:
        response == "Kill Sherlock Holmes and his companions"
    }

    @Requires(property = "spec.name", value = "PlanControllerAuthenticatedWithRolesSpec") // <1>
    @Singleton
    static class MockAuthenticationFetcher implements AuthenticationFetcher<HttpRequest<?>> {

        @Override
        Publisher<Authentication> fetchAuthentication(HttpRequest<?> request) {
            Publishers.just(Authentication.build("moriarty", ["ROLE_EVIL_MASTERMIND"]))
        }
    }
}
