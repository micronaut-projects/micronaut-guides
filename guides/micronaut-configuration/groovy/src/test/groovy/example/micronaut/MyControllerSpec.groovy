package example.micronaut

import io.micronaut.http.HttpRequest
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import spock.lang.Specification

import jakarta.inject.Inject

@MicronautTest
class MyControllerSpec extends Specification {

    @Inject
    @Client("/")
    HttpClient client

    void 'test my team'() {
        when:
        TeamConfiguration teamConfiguration = client.toBlocking().retrieve(HttpRequest.GET("/my/team"), TeamConfiguration)

        then:
        teamConfiguration.name == 'Steelers'
        teamConfiguration.color == 'Black'

        and:
        teamConfiguration.playerNames.size() == 2
        teamConfiguration.playerNames == ['Mason Rudolph', 'James Connor']
    }
}
