package example.micronaut;

import io.micronaut.mqtt.annotation.MqttSubscriber;
import io.micronaut.mqtt.annotation.Topic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;

import static java.nio.charset.StandardCharsets.UTF_8;

@MqttSubscriber
public class TemperatureListener {

    private static final Logger LOG = LoggerFactory.getLogger(TemperatureListener.class);

    @Topic("house/livingroom/temperature")
    public void receive(byte[] data) {
        BigDecimal temperature = new BigDecimal(new String(data, UTF_8));
        LOG.info("temperature: {}", temperature);
    }
}
