/*
 * Copyright 2017-2024 original authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package example.micronaut

import example.micronaut.auth.Credentials
import example.micronaut.models.Item
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import spock.lang.Specification

import static io.micronaut.http.HttpStatus.UNAUTHORIZED

@MicronautTest // <1>
class ItemsControllerSpec extends Specification {

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

    void "get item"() {
        def authHeader = "Basic " + Base64.getEncoder().encodeToString((credentials.username + ":" + credentials.password).getBytes())
        def itemId = 1

        when:
        Item item = orderItemClient.getItemsById(authHeader, itemId)

        then:
        itemId == item.id
        item.name == "Banana"
        item.price == new BigDecimal("1.5")
    }

    void "get items"() {
        def authHeader = "Basic " + Base64.getEncoder().encodeToString((credentials.username + ":" + credentials.password).getBytes())
        def existingItemNames = ["Kiwi", "Banana", "Grape"]

        when:
        def items = orderItemClient.getItems(authHeader)

        then:
        items != null
        items.size() == 3
        items.stream().allMatch(y -> existingItemNames.stream().anyMatch(x -> x == y.name))
    }

}
