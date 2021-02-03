package example.micronaut

import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import javax.inject.Named

@Controller("/my")
class MyController(val teamConfiguration: TeamConfiguration,
                   @Named("pnc") val stadiumConfiguration: StadiumConfiguration) {

    @Get("/team")
    fun team(): TeamConfiguration {
        return teamConfiguration
    }

    @Get("/stadium")
    fun stadium(): StadiumConfiguration {
        return stadiumConfiguration
    }
}