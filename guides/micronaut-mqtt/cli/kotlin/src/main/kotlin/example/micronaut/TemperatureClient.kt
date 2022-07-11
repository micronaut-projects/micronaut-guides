package example.micronaut

import io.micronaut.mqtt.annotation.Topic
import io.micronaut.mqtt.v5.annotation.MqttPublisher

@MqttPublisher // <1>
interface TemperatureClient {

    @Topic("house/livingroom/temperature") // <2>
    fun publishLivingroomTemperature(data: ByteArray)
}
