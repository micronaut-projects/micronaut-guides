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

import io.micronaut.core.annotation.Nullable
import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import io.micronaut.mqtt.annotation.MqttSubscriber
import io.micronaut.mqtt.annotation.Topic

import static java.nio.charset.StandardCharsets.UTF_8

@Slf4j
@CompileStatic
@MqttSubscriber // <1>
class TemperatureListener {

    @Nullable
    BigDecimal temperature = null;

    @Topic('house/livingroom/temperature') // <2>
    void receive(byte[] data) {
        temperature = new BigDecimal(new String(data, UTF_8))
        log.info('temperature: {}', temperature)
    }
}
