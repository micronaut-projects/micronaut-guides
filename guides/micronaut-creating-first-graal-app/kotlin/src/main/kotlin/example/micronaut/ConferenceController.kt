package example.micronaut

import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get

@Controller("/conferences") // <1>
class ConferenceController(private val conferenceService: ConferenceService) { // <2>

    @Get("/random") // <3>
    fun randomConf(): Conference = conferenceService.randomConf() // <4>
}
