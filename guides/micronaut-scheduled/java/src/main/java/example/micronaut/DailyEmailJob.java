package example.micronaut;

import io.micronaut.scheduling.annotation.Scheduled;

import javax.inject.Singleton;

@Singleton // <1>
public class DailyEmailJob {

    protected final EmailUseCase emailUseCase;

    public DailyEmailJob(EmailUseCase emailUseCase) {  // <2>
        this.emailUseCase = emailUseCase;
    }

    @Scheduled(cron = "0 30 4 1/1 * ?") // <3>
    void execute() {
        emailUseCase.send("john.doe@micronaut.example", "Test Message"); // <4>
    }
}
