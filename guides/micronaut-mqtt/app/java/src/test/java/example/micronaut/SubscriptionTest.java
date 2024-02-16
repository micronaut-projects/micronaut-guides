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
package example.micronaut;

import io.micronaut.context.annotation.Property;
import io.micronaut.context.annotation.Requires;
import io.micronaut.mqtt.annotation.Topic;
import io.micronaut.mqtt.annotation.v5.MqttPublisher;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertEquals;

@MicronautTest // <1>
@Property(name = "spec.name", value = "SubscriptionTest") // <2>
class SubscriptionTest {

    @Inject
    TemperatureClient client;

    @Inject
    TemperatureListener listener;

    @Test
    void checkSubscriptionsAreReceived() {
        client.publishLivingroomTemperature("3.145".getBytes(StandardCharsets.UTF_8));

        await().atMost(5, TimeUnit.SECONDS)
                .untilAsserted(() -> assertEquals(new BigDecimal("3.145"), listener.getTemperature()));
    }

    @Requires(property = "spec.name", value = "SubscriptionTest") // <3>
    @MqttPublisher // <4>
    static interface TemperatureClient {

        @Topic("house/livingroom/temperature") // <5>
        void publishLivingroomTemperature(byte[] data);
    }
}
