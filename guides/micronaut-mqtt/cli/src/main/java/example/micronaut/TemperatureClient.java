package example.micronaut;

import io.micronaut.mqtt.annotation.Topic;
import io.micronaut.mqtt.v5.annotation.MqttPublisher;
import java.math.BigDecimal;

@MqttPublisher
public interface TemperatureClient {

    @Topic("house/livingroom/temperature")
    void publishLivingroomTemperature(byte[] data);
}
