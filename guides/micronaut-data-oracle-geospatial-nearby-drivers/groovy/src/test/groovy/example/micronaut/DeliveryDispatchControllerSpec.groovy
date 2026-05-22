/*
 * Copyright 2017-2026 original authors
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

import io.micronaut.data.model.geo.Point
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.BlockingHttpClient
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import spock.lang.Specification

@MicronautTest(transactional = false) // <1>
class DeliveryDispatchControllerSpec extends Specification {

    @Inject
    DeliveryDriverRepository deliveryDriverRepository

    @Inject
    @Client('/') // <2>
    HttpClient httpClient

    void setup() {
        deliveryDriverRepository.deleteAll()
    }

    void "finds closest available driver within five kilometers"() {
        given:
        deliveryDriverRepository.save(new DeliveryDriver(
            'Nearby Driver',
            DeliveryDriver.AVAILABLE,
            new Point(-73.9757d, 40.7554d)
        ))
        DeliveryDriver closest = deliveryDriverRepository.save(new DeliveryDriver(
            'Closest Driver',
            DeliveryDriver.AVAILABLE,
            new Point(-73.9827d, 40.7504d)
        ))
        deliveryDriverRepository.save(new DeliveryDriver(
            'Busy Driver',
            DeliveryDriver.BUSY,
            new Point(-73.9850d, 40.7488d)
        )) // <3>
        deliveryDriverRepository.save(new DeliveryDriver(
            'Far Driver',
            DeliveryDriver.AVAILABLE,
            new Point(-73.9000d, 40.8000d)
        )) // <4>

        when:
        BlockingHttpClient client = httpClient.toBlocking()
        HttpResponse<DriverMatch> response = client.exchange(
            HttpRequest.GET('/orders/nearest-driver?longitude=-73.9857&latitude=40.7484'),
            DriverMatch
        ) // <5>

        then:
        response.status() == HttpStatus.OK
        response.body().driverId == closest.id // <6>
    }

    void "returns not found when no available driver is close enough"() {
        given:
        deliveryDriverRepository.save(new DeliveryDriver(
            'Busy Driver',
            DeliveryDriver.BUSY,
            new Point(-73.9850d, 40.7488d)
        ))
        deliveryDriverRepository.save(new DeliveryDriver(
            'Far Driver',
            DeliveryDriver.AVAILABLE,
            new Point(-73.9000d, 40.8000d)
        ))

        when:
        BlockingHttpClient client = httpClient.toBlocking()
        client.exchange(
            HttpRequest.GET('/orders/nearest-driver?longitude=-73.9857&latitude=40.7484'),
            DriverMatch
        ) // <7>

        then:
        HttpClientResponseException thrown = thrown()
        thrown.status == HttpStatus.NOT_FOUND
    }
}
