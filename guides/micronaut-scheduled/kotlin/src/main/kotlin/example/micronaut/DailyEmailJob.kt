package example.micronaut

import io.micronaut.scheduling.annotation.Scheduled
import javax.inject.Singleton

@Singleton // <1>
class DailyEmailJob(protected val emailUseCase: EmailUseCase) { // <2>

    @Scheduled(cron = "0 30 4 1/1 * ?") // <3>
    fun execute() {
        emailUseCase.send("john.doe@micronaut.example", "Test Message") // <4>
    }
}
