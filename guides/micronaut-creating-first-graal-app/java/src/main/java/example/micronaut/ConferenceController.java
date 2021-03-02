package example.micronaut;

import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;

@Controller("/conferences") // <1>
public class ConferenceController {

    private final ConferenceService conferenceService;

    public ConferenceController(ConferenceService conferenceService) { // <2>
        this.conferenceService = conferenceService;
    }

    @Get("/random") // <3>
    public Conference randomConf() { // <4>
        return conferenceService.randomConf();
    }
}
