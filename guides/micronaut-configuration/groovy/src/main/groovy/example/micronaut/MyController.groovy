package example.micronaut

import groovy.transform.CompileStatic
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get

import javax.inject.Named

@CompileStatic
@Controller("/my")
class MyController {

    TeamConfiguration teamConfiguration
    StadiumConfiguration stadiumConfiguration

    MyController(TeamConfiguration teamConfiguration,
                 @Named("pnc") StadiumConfiguration stadiumConfiguration) { // <1>
        this.teamConfiguration = teamConfiguration
        this.stadiumConfiguration = stadiumConfiguration
    }

    @Get("/team")
    TeamConfiguration team() {
        this.teamConfiguration
    }

    @Get("/stadium")
    StadiumConfiguration stadium() {
        this.stadiumConfiguration
    }
}
