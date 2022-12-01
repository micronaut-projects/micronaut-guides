package example.micronaut

import example.micronaut.clients.OrdersClient
import example.micronaut.clients.UsersClient
import example.micronaut.models.Item
import example.micronaut.models.Order
import example.micronaut.models.User
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.test.annotation.MockBean
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import spock.lang.Specification

@MicronautTest // <1>
class GatewayControllerSpec extends Specification {

    @Inject
    OrdersClient ordersClient

    @Inject
    UsersClient usersClient

    @Inject
    GatewayClient gatewayClient


    @MockBean(OrdersClient.class)
    OrdersClient ordersClient() {
        return Mock(OrdersClient.class)
    }

    @MockBean(UsersClient.class)
    UsersClient usersClient() {
        return Mock(UsersClient.class)
    }

    void 'get item by id'() {
        given:
        def itemId = 1
        def item = new Item(itemId, "test", BigDecimal.ONE)

        when:
        def retrievedItem = gatewayClient.getItemById(1)

        then:
        1 * ordersClient.getItemsById(1) >> item

        item.id == retrievedItem.id
        item.name == retrievedItem.name
        item.price == retrievedItem.price
    }

    void 'get order by id'() {
        given:
        def order = new Order(1, 2, null, null, new ArrayList<>(), null)
        def user = new User(order.userId, "firstName", "lastName", "test")

        when:
        def retrievedOrder = gatewayClient.getOrderById(order.id)

        then:
        1 * ordersClient.getOrderById(1) >> order
        1 * usersClient.getById(user.id) >> user

        order.id == retrievedOrder.id
        order.userId == retrievedOrder.user.id
        retrievedOrder.userId == null
        user.username == retrievedOrder.user.username
    }

    void 'get user by id'() {
        given:
        def user = new User(1, "firstName", "lastName", "test")

        when:
        def retrievedUser = gatewayClient.getUsersById(user.id)

        then:
        1 * usersClient.getById(user.id) >> user
        user.id == retrievedUser.id
        user.username == retrievedUser.username
    }

    void 'get users'() {
        given:
        def user = new User(1, "firstName", "lastName", "test")

        when:
        def users = gatewayClient.getUsers()

        then:
        1 * usersClient.getUsers() >> [user]
        users.size() == 1
        user.id == users[0].id
        user.username == users[0].username
    }

    void 'get items'() {
        given:
        def item = new Item(1, "test", BigDecimal.ONE)

        when:
        def items = gatewayClient.getItems()

        then:
        1 * ordersClient.getItems() >> [item]
        items.size() == 1
        item.id == items[0].id
        item.name == items[0].name
        item.price == items[0].price
    }

    void 'get orders'() {
        given:
        def order = new Order(1, 2, null, null, new ArrayList<>(), null)
        def user = new User(order.userId, "firstName", "lastName", "test")

        when:
        def orders = gatewayClient.getOrders()

        then:
        1 * ordersClient.getOrders() >> [order]
        1 * usersClient.getById(user.id) >> user

        orders.size() == 1
        order.id == orders[0].id
        order.userId == orders[0].user.id
        orders[0].userId == null
        user.username == orders[0].user.username
    }

    void 'create user'() {
        given:
        def user = new User(0, "firstName", "lastName", "username")

        when:
        def createdUser = gatewayClient.createUser(user)

        then:
        1 * usersClient.createUser(_) >> user

        user.firstName == createdUser.firstName
        user.lastName == createdUser.lastName
        user.username == createdUser.username

    }

    void 'create order'() {
        given:
        def order = new Order(1, 2, null, null, new ArrayList<>(), null)
        def user = new User(order.userId, "firstName", "lastName", "test")

        when:
        def createdOrder = gatewayClient.createOrder(order)

        then:
        1 * usersClient.getById(user.id) >> user
        1 * ordersClient.createOrder(_) >> order
        order.id == createdOrder.id
        createdOrder.userId == null
        order.userId == createdOrder.user.id
        user.username == createdOrder.user.username
    }

    void 'create order user doesnt exists'() {
        given:
        def order = new Order(1, 2, null, null, new ArrayList<>(), null)

        when:
        gatewayClient.createOrder(order)

        then:
        1 * usersClient.getById(_) >> null

        HttpClientResponseException e = thrown()
        e.response.status == HttpStatus.BAD_REQUEST
        e.response.getBody(String.class).orElse("").contains("User with id 2 doesn't exist")


    }

    void 'exception handler'() {
        given:
        def user = new User(1, "firstname", "lastname", "username")
        def message = "Test error message"

        when:
        gatewayClient.createUser(user)

        then:
        1 * usersClient.createUser(_) >> {throw new HttpClientResponseException("Test", HttpResponse.badRequest(message))}

        HttpClientResponseException e = thrown()
        e.response.status == HttpStatus.BAD_REQUEST
        e.response.getBody(String.class).orElse("").contains(message)
    }

}
