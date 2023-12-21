/*
 * Copyright 2017-2023 original authors
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

import io.micronaut.context.annotation.Property
import io.micronaut.context.annotation.Requires
import io.micronaut.mqtt.annotation.Topic
import io.micronaut.mqtt.annotation.v5.MqttPublisher
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import spock.util.concurrent.PollingConditions

import java.nio.charset.StandardCharsets
import spock.lang.Specification

@MicronautTest // <1>
@Property(name = "spec.name", value = "SubscriptionTest") // <2>
class SubscriptionSpec extends Specification {

    @Inject
    TemperatureClient client

    @Inject
    TemperatureListener listener

    PollingConditions conditions = new PollingConditions()

    def "subscriptions are received"() {
        when:
        client.publishLivingroomTemperature("3.145".getBytes(StandardCharsets.UTF_8))

        then:
        conditions.within(5) {
            listener.temperature == 3.145
        }
    }

    @Requires(property = "spec.name", value = "SubscriptionTest") // <3>
    @MqttPublisher // <4>
    static interface TemperatureClient {

        @Topic("house/livingroom/temperature") // <5>
        void publishLivingroomTemperature(byte[] data)
    }
}
