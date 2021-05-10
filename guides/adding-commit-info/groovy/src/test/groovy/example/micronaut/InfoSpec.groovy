package example.micronaut

import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.client.RxHttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import spock.lang.Shared
import spock.lang.Specification
import javax.inject.Inject

@MicronautTest // <1>
class InfoSpec extends Specification {

    @Shared
    @Client("/")
    @Inject
    RxHttpClient client // <2>

    void 'test git commit info appears in JSON'() {
        given:
        HttpRequest request = HttpRequest.GET('/info') // <3>

        when:
        HttpResponse<Map> rsp = client.toBlocking().exchange(request, Map)

        then:
        rsp.status().code == 200

        when:
        Map json = rsp.body() // <4>

        then:
        json.git
        json.git.commit
        json.git.commit.message
        json.git.commit.time
        json.git.commit.id
        json.git.commit.user
        json.git.branch
    }
}