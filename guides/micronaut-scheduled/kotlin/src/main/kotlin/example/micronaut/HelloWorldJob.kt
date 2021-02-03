package example.micronaut

import io.micronaut.scheduling.annotation.Scheduled
import org.slf4j.LoggerFactory
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Singleton

@Singleton // <1>
class HelloWorldJob {

    @Scheduled(fixedDelay = "10s") // <3>
    fun executeEveryTen() {
        LOG.info("Simple Job every 10 seconds: {}", SimpleDateFormat("dd/M/yyyy hh:mm:ss").format(Date()))
    }

    @Scheduled(fixedDelay = "45s", initialDelay = "5s") // <4>
    fun executeEveryFourtyFive() {
        LOG.info("Simple Job every 45 seconds: {}", SimpleDateFormat("dd/M/yyyy hh:mm:ss").format(Date()))
    }

    companion object {
        private val LOG = LoggerFactory.getLogger(HelloWorldJob::class.java) // <2>
    }
}
