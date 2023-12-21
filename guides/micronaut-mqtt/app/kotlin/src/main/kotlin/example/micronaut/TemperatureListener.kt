/*
 * Copyright 2017-2023 original authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package example.micronaut

import io.micronaut.mqtt.annotation.MqttSubscriber
import io.micronaut.mqtt.annotation.Topic
import org.slf4j.LoggerFactory
import java.math.BigDecimal
import java.nio.charset.StandardCharsets.UTF_8
import io.micronaut.core.annotation.Nullable

@MqttSubscriber // <1>
class TemperatureListener {

    var temperature: BigDecimal? = null

    @Topic("house/livingroom/temperature") // <2>
    fun receive(data: ByteArray) {
        temperature = BigDecimal(String(data, UTF_8))
        LOG.info("temperature: {}", temperature)
    }

    companion object {
        private val LOG = LoggerFactory.getLogger(TemperatureListener::class.java)
    }
}
