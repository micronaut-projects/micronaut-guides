package example.micronaut

import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import io.micronaut.scheduling.TaskExecutors
import io.micronaut.scheduling.TaskScheduler

import javax.inject.Named
import javax.inject.Singleton
import java.text.SimpleDateFormat
import java.time.Duration

@CompileStatic
@Slf4j
@Singleton
class RegisterUseCase {

    protected final TaskScheduler taskScheduler
    protected final EmailUseCase emailUseCase

    RegisterUseCase(EmailUseCase emailUseCase, // <1>
                    @Named(TaskExecutors.SCHEDULED) TaskScheduler taskScheduler) { // <2>
        this.emailUseCase = emailUseCase
        this.taskScheduler = taskScheduler
    }

    void register(String email) {
        log.info("saving {} at {}", email, new SimpleDateFormat("dd/M/yyyy hh:mm:ss").format(new Date()))
        scheduleFollowupEmail(email, "Welcome to Micronaut")
    }

    private void scheduleFollowupEmail(String email, String message) {
        EmailTask task = new EmailTask(emailUseCase: emailUseCase, email: email, message: message) // <3>
        taskScheduler.schedule(Duration.ofMinutes(1), task) // <4>
    }
}