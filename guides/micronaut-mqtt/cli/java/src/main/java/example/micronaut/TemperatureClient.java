package example.micronaut;

import io.micronaut.mqtt.annotation.Topic;
import io.micronaut.mqtt.v5.annotation.MqttPublisher;

@MqttPublisher // <1>
public interface TemperatureClient {

    @Topic("house/livingroom/temperature") // <2>
    void publishLivingroomTemperature(byte[] data);
}
