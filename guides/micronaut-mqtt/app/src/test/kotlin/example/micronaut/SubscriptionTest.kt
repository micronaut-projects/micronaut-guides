package example.micronaut

import io.micronaut.context.annotation.Property
import io.micronaut.context.annotation.Requires
import io.micronaut.mqtt.annotation.Topic
import io.micronaut.mqtt.annotation.v5.MqttPublisher
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import org.junit.jupiter.api.Test

import java.math.BigDecimal
import java.nio.charset.StandardCharsets
import java.util.concurrent.TimeUnit

import static org.awaitility.Awaitility.await
import static org.junit.jupiter.api.Assertions.assertEquals

@MicronautTest // <1>
@Property(name = "spec.name", value = "SubscriptionTest") // <2>
class SubscriptionTest {

    @Inject
    TemperatureClient client

    @Inject
    TemperatureListener listener

    @Test
    void checkSubscriptionsAreReceived() {
        client.publishLivingroomTemperature("3.145".bytes)

        await().atMost(5, TimeUnit.SECONDS)
                .untilAsserted(() -> assertEquals(BigDecimal("3.145"), listener.temperature))
    }

    @Requires(property = "spec.name", value = "SubscriptionTest") // <3>
    @MqttPublisher // <4>
    static interface TemperatureClient {

        @Topic("house/livingroom/temperature") // <5>
        fun publishLivingroomTemperature(data: ByteArray)
    }
}
