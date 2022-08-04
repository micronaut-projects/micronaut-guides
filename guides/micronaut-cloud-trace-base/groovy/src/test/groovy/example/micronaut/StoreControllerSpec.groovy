package example.micronaut

import io.micronaut.core.type.Argument
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import spock.lang.Specification

import jakarta.inject.Inject

@MicronautTest
class StoreControllerSpec extends Specification {

    @Inject
    @Client("/store")
    HttpClient client

    void 'test inventory item'() {
        given:
        def request = HttpRequest.GET('/inventory/laptop')

        when:
        def inventory = client.toBlocking().retrieve(request, HashMap.class)

        then:
        inventory
        inventory.'item'  == 'laptop'
        inventory.'store' == 4
        inventory.'warehouse'
    }

    void 'test inventory item not found'() {
        when:
        def inventory = client.toBlocking().retrieve("/inventory/chair", HashMap.class)

        then:
        inventory
        inventory.'item' == 'chair'
        inventory.'note' == 'Not available at store'
    }

    void 'test inventory all'() {
        given:
        def request = HttpRequest.GET('/inventory')

        when:
        def inventory = client.toBlocking().retrieve(request, Argument.listOf(HashMap.class))

        then:
        inventory
        inventory.size() == 3

        when:
        def names = inventory.collect{ it.'item' }

        then:
        names.containsAll(['desktop', 'monitor', 'laptop'])
    }

    void 'test order'() {
        given:
        def item = [item:'desktop', count:8]
        def request = HttpRequest.POST('/order', item)

        when:
        def response = client.toBlocking().exchange(request)

        then:
        response.status == HttpStatus.CREATED

        when:
        def inventory = client.toBlocking().retrieve('/inventory/desktop', HashMap.class)

        then:
        inventory
        inventory.'item'  == 'desktop'
        inventory.'store' == 10
        !inventory.'warehouse'
    }
}