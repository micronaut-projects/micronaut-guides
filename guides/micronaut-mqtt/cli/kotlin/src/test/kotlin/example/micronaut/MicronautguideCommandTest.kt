package example.micronaut

import io.micronaut.configuration.picocli.PicocliRunner
import io.micronaut.context.ApplicationContext
import io.micronaut.context.annotation.Replaces
import io.micronaut.context.annotation.Requires
import io.micronaut.context.env.Environment
import jakarta.inject.Singleton
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.io.ByteArrayOutputStream
import java.io.OutputStream
import java.io.PrintStream
import java.math.BigDecimal
import java.nio.charset.StandardCharsets.UTF_8
import java.util.concurrent.TimeUnit
import org.awaitility.Awaitility.await

class MicronautguideCommandTest {

    @Test
    fun testWithCommandLineOption() {
        val baos: OutputStream = ByteArrayOutputStream()
        System.setOut(PrintStream(baos))

        ApplicationContext.run(
                mapOf("mqtt.enabled" to false, "spec.name" to "MicronautguideCommandTest"),
                Environment.CLI, Environment.TEST).use { ctx ->

            val listener: TemperatureListener = ctx.getBean(
                TemperatureListener::class.java
            )

            val args = arrayOf("-t", "212", "-s", "Fahrenheit")
            PicocliRunner.run(MicronautguideCommand::class.java, ctx, *args)
            assertTrue(baos.toString().contains("Topic published"))

            val temperatures = ctx.getBean(TemperatureClientReplacement::class.java).getTemperatures()
            assertEquals(listOf(BigDecimal("100.00")), temperatures)
        }
    }

    @Requires(property = "spec.name", value = "MicronautguideCommandTest")
    @Replaces(TemperatureClient::class)
    @Singleton
    internal class TemperatureClientReplacement : TemperatureClient {

        private val temperatures = mutableListOf<ByteArray>()

        override fun publishLivingroomTemperature(data: ByteArray) {
            temperatures.add(data)
        }

        fun getTemperatures(): List<BigDecimal> =
                temperatures.map { bytes: ByteArray -> BigDecimal(String(bytes, UTF_8)) }
    }

    @Requires(property = "spec.name", value = "MicronautguideCommandTest")
    @MqttSubscriber // <1>
    class TemperatureListener {
        var temperature: BigDecimal? = null

        @Topic("house/livingroom/temperature") // <2>
        fun receive(data: ByteArray?) {
            temperature = BigDecimal(String(data, java.nio.charset.StandardCharsets.UTF_8))
        }
    }
}
