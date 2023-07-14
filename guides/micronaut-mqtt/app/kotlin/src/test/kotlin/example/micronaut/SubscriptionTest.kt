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