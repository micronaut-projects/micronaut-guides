package example.micronaut

import example.micronaut.auth.Credentials
import example.micronaut.models.Order
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import spock.lang.Specification

import static io.micronaut.http.HttpStatus.BAD_REQUEST
import static io.micronaut.http.HttpStatus.UNAUTHORIZED

@MicronautTest // <1>
class OrdersControllerSpec extends Specification {

    @Inject
    OrderItemClient orderItemClient

    @Inject
    Credentials credentials

    void "unauthorized"() {

        when:
        orderItemClient.getItems("")

        then:
        HttpClientResponseException e = thrown()
        e.response.status == UNAUTHORIZED
        e.message.contains("Unauthorized")
    }

    void "multiple order interaction"() {
        String authHeader = "Basic " + Base64.getEncoder().encodeToString((credentials.username + ":" + credentials.password).getBytes())
        def userId = 1
        def itemIds = [1, 1, 2, 3]
        Order order = new Order(0, userId, null, itemIds, null)

        when:
        def createdOrder = orderItemClient.createOrder(authHeader, order)

        then:
        createdOrder.items != null
        createdOrder.items.size() == 4
        createdOrder.total == new BigDecimal("6.75")
        createdOrder.userId == userId

        when:
        def retrievedOrder = orderItemClient.getOrderById(authHeader, createdOrder.id)

        then:
        retrievedOrder.items != null
        retrievedOrder.items.size() == 4
        retrievedOrder.total == new BigDecimal("6.75")
        retrievedOrder.userId == userId

        when:
        def orders = orderItemClient.getOrders(authHeader)

        then:
        orders != null
        orders.stream().anyMatch(x -> x.userId == userId)
    }

    void "item doesn't exist"() {
        def authHeader = "Basic " + Base64.getEncoder().encodeToString((credentials.username + ":" + credentials.password).getBytes())

        def  userId = 1
        def itemIds = [5]

        def order = new Order(0, userId, null, itemIds, null)

        when:
        orderItemClient.createOrder(authHeader, order)

        then:
        HttpClientResponseException e = thrown()
        e.response.status == BAD_REQUEST
        e.response.getBody(String.class).orElse("").contains("Item with id 5 doesn't exist")
    }

    void "order empty items"() {
        def authHeader = "Basic " + Base64.getEncoder().encodeToString((credentials.username + ":" + credentials.password).getBytes())

        def  userId = 1
        def itemIds = [5]

        def order = new Order(0, userId, null, null, null)

        when:
        orderItemClient.createOrder(authHeader, order)

        then:
        HttpClientResponseException e = thrown()
        e.response.status == BAD_REQUEST
        e.response.getBody(String.class).orElse("").contains("Items must be supplied")
    }

}
