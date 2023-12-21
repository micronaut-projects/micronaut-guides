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
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import org.awaitility.Awaitility
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.nio.charset.StandardCharsets
import java.util.concurrent.TimeUnit

@MicronautTest // <1>
@Property(name = "spec.name", value = "SubscriptionTest") // <2>
internal class SubscriptionTest {

    @Inject
    var client: TemperatureClient? = null

    @Inject
    var listener: TemperatureListener? = null

    @Test
    fun checkSubscriptionsAreReceived() {
        client!!.publishLivingroomTemperature("3.145".toByteArray(StandardCharsets.UTF_8))
        Awaitility.await().atMost(5, TimeUnit.SECONDS)
            .untilAsserted {
                Assertions.assertEquals(
                    BigDecimal("3.145"),
                    listener!!.temperature
                )
            }
    }

    @Requires(property = "spec.name", value = "SubscriptionTest") // <3>
    @MqttPublisher // <4>
    internal interface TemperatureClient {
        @Topic("house/livingroom/temperature") // <5>
        fun publishLivingroomTemperature(data: ByteArray?)
    }
}