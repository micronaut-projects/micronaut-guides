package example.micronaut

import io.micronaut.context.annotation.Property
import io.micronaut.context.annotation.Requires
import io.micronaut.mqtt.annotation.Topic
import io.micronaut.mqtt.v5.annotation.MqttPublisher
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject

import java.nio.charset.StandardCharsets
import java.util.concurrent.TimeUnit
import spock.lang.Specification
import static org.awaitility.Awaitility.await

@MicronautTest // <1>
@Property(name = "spec.name", value = "SubscriptionTest") // <2>
class SubscriptionSpec extends Specification {

    @Inject
    TemperatureClient client

    @Inject
    TemperatureListener listener

    def "subscriptions are received"() {
        when:
        client.publishLivingroomTemperature("3.145".getBytes(StandardCharsets.UTF_8))

        then:
        await().atMost(5, TimeUnit.SECONDS).until { listener.temperature == 3.145 }
    }

    @Requires(property = "spec.name", value = "SubscriptionTest") // <3>
    @MqttPublisher // <4>
    static interface TemperatureClient {

        @Topic("house/livingroom/temperature") // <5>
        void publishLivingroomTemperature(byte[] data)
    }
}
