/*
 * Copyright 2017-2024 original authors
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

import io.micronaut.configuration.picocli.PicocliRunner
import io.micronaut.context.ApplicationContext
import io.micronaut.context.annotation.Requires
import io.micronaut.context.env.Environment
import io.micronaut.mqtt.annotation.MqttSubscriber
import io.micronaut.mqtt.annotation.Topic
import org.awaitility.Awaitility
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.io.ByteArrayOutputStream
import java.io.OutputStream
import java.io.PrintStream
import java.math.BigDecimal
import java.nio.charset.StandardCharsets
import java.util.concurrent.TimeUnit

internal class MicronautguideCommandTest {

    @Test
    fun testWithCommandLineOption() {
        val baos: OutputStream = ByteArrayOutputStream()
        System.setOut(PrintStream(baos))
        ApplicationContext.run(
            mapOf("spec.name" to "MicronautguideCommandTest"),
            Environment.CLI, Environment.TEST
        ).use { ctx ->
            val listener: TemperatureListener = ctx.getBean(TemperatureListener::class.java)
            val args = arrayOf("-t", "212", "-s", "Fahrenheit")
            PicocliRunner.run(MicronautguideCommand::class.java, ctx, *args)
            Assertions.assertTrue(baos.toString().contains("Topic published"))

            Awaitility.await().atMost(5, TimeUnit.SECONDS)
                .untilAsserted {
                    Assertions.assertEquals(
                        BigDecimal("100.00"),
                        listener.temperature
                    )
                }
        }
    }

    @Requires(property = "spec.name", value = "MicronautguideCommandTest")
    @MqttSubscriber // <1>
    class TemperatureListener {
        var temperature: BigDecimal? = null

        @Topic("house/livingroom/temperature") // <2>
        fun receive(data: ByteArray?) {
            temperature = BigDecimal(String(data!!, StandardCharsets.UTF_8))
        }
    }
}