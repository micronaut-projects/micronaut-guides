package example.micronaut;

import io.micronaut.mqtt.annotation.MqttSubscriber;
import io.micronaut.mqtt.annotation.Topic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;

@MqttSubscriber // <1>
public class TemperatureListener {
    private static final Logger LOG = LoggerFactory.getLogger(TemperatureListener.class);

    @Topic("house/livingroom/temperature") // <2>
    public void receive(byte[] data) {
        BigDecimal temperature = new BigDecimal(new String(data, StandardCharsets.UTF_8));
        LOG.info("temperature: {}", temperature);
    }

}
