package example.micronaut;

import io.micronaut.mqtt.annotation.Topic;
import io.micronaut.mqtt.v5.annotation.MqttPublisher;
import java.math.BigDecimal;

@MqttPublisher // <1>
public interface TemperatureClient {

    @Topic("house/livingroom/temperature") // <1>
    void publishLivingroomTemperature(byte[] data);
}
