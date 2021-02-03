package example.micronaut

import groovy.transform.CompileStatic
import io.micronaut.scheduling.annotation.Scheduled

import javax.inject.Singleton

@CompileStatic
@Singleton // <1>
class DailyEmailJob {

    protected final EmailUseCase emailUseCase

    DailyEmailJob(EmailUseCase emailUseCase) { // <2>
        this.emailUseCase = emailUseCase
    }

    @Scheduled(cron = "0 30 4 1/1 * ?") // <3>
    void execute() {
        emailUseCase.send("john.doe@micronaut.example", "Test Message") // <4>
    }
}
