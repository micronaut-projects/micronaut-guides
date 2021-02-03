package example.micronaut

import io.micronaut.scheduling.TaskExecutors
import io.micronaut.scheduling.TaskScheduler
import org.slf4j.LoggerFactory
import java.text.SimpleDateFormat
import java.time.Duration
import java.util.*
import javax.inject.Named
import javax.inject.Singleton

@Singleton
class RegisterUseCase(private val emailUseCase: EmailUseCase, // <1>
                      @param:Named(TaskExecutors.SCHEDULED) private val taskScheduler: TaskScheduler) { // <2>

    fun register(email: String) {
        LOG.info("saving {} at {}", email, SimpleDateFormat("dd/M/yyyy hh:mm:ss").format(Date()))
        scheduleFollowupEmail(email, "Welcome to Micronaut")
    }

    private fun scheduleFollowupEmail(email: String, message: String) {
        val task = EmailTask(emailUseCase, email, message) // <3>
        taskScheduler.schedule(Duration.ofMinutes(1), task)  // <4>
    }

    companion object {
        private val LOG = LoggerFactory.getLogger(RegisterUseCase::class.java)
    }
}
