package example.micronaut

import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j

import javax.inject.Singleton
import java.text.SimpleDateFormat

@Slf4j
@CompileStatic
@Singleton
class EmailUseCase {

    void send(String user, String message) {
        log.info("Sending email to {}: {} at {}", user, message, new SimpleDateFormat("dd/M/yyyy hh:mm:ss").format(new Date()))
    }
}