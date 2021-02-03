package example.micronaut

import org.slf4j.LoggerFactory
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Singleton

@Singleton
class EmailUseCase {

    fun send(user: String, message: String) {
        LOG.info("Sending email to {} : {} at {}", user, message, SimpleDateFormat("dd/M/yyyy hh:mm:ss").format(Date()))
    }

    companion object {
        private val LOG = LoggerFactory.getLogger(EmailUseCase::class.java)
    }
}
