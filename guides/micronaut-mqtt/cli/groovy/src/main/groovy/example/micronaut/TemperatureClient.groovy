package example.micronaut

import io.micronaut.mqtt.annotation.Topic
import io.micronaut.mqtt.v5.annotation.MqttPublisher

@MqttPublisher
interface TemperatureClient {

    @Topic('house/livingroom/temperature')
    void publishLivingroomTemperature(byte[] data)
}
