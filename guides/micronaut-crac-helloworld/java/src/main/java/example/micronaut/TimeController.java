package example.micronaut;

import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.runtime.context.scope.Refreshable;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Map;

@Refreshable // <1>
@Controller("/time") // <1>
class TimeController {
    private final String time;
    TimeController() {
        time = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME);
    }

    @Get // <3>
    Map<String, Object> index() {
        return Collections.singletonMap("time", time);
    }
}
