package example.micronaut

import io.micronaut.mqtt.annotation.MqttSubscriber
import io.micronaut.mqtt.annotation.Topic
import org.slf4j.LoggerFactory
import java.math.BigDecimal
import java.nio.charset.StandardCharsets.UTF_8
import io.micronaut.core.annotation.Nullable

@MqttSubscriber // <1>
class TemperatureListener {

    val temperature: BigDecimal? = null

    @Topic("house/livingroom/temperature") // <2>
    fun receive(data: ByteArray) {
        temperature = BigDecimal(String(data, UTF_8))
        LOG.info("temperature: {}", temperature)
    }

    companion object {
        private val LOG = LoggerFactory.getLogger(TemperatureListener::class.java)
    }
}
