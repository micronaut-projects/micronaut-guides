package example.micronaut;

import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.TaskScheduler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Named;
import javax.inject.Singleton;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;

@Singleton
public class RegisterUseCase {

    private static final Logger LOG = LoggerFactory.getLogger(RegisterUseCase.class);

    protected final TaskScheduler taskScheduler;
    protected final EmailUseCase emailUseCase;

    public RegisterUseCase(EmailUseCase emailUseCase, // <1>
                           @Named(TaskExecutors.SCHEDULED) TaskScheduler taskScheduler) {  // <2>
        this.emailUseCase = emailUseCase;
        this.taskScheduler = taskScheduler;
    }

    public void register(String email) {
        LOG.info("saving {} at {}", email, new SimpleDateFormat("dd/M/yyyy hh:mm:ss").format(new Date()));
        scheduleFollowupEmail(email, "Welcome to Micronaut");
    }

    private void scheduleFollowupEmail(String email, String message) {
        EmailTask task = new EmailTask(emailUseCase, email, message); // <3>
        taskScheduler.schedule(Duration.ofMinutes(1), task);  // <4>
    }
}
