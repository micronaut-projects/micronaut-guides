package example.micronaut

import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import io.micronaut.scheduling.annotation.Scheduled

import javax.inject.Singleton
import java.text.SimpleDateFormat

@CompileStatic
@Singleton // <1>
@Slf4j // <2>
class HelloWorldJob {

    @Scheduled(fixedDelay = "10s") // <3>
    void executeEveryTen() {
        log.info("Simple Job every 10 seconds: {}", new SimpleDateFormat("dd/M/yyyy hh:mm:ss").format(new Date()))
    }

    @Scheduled(fixedDelay = "45s", initialDelay = "5s") // <4>
    void executeEveryFortyFive() {
        log.info("Simple Job every 45 seconds: {}", new SimpleDateFormat("dd/M/yyyy hh:mm:ss").format(new Date()))
    }
}
