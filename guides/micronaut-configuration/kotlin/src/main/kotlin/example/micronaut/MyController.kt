package example.micronaut

import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import jakarta.inject.Named

@Controller("/my")
class MyController(val teamConfiguration: TeamConfiguration,
                   @Named("pnc") val stadiumConfiguration: StadiumConfiguration) { // <1>

    @Get("/team")
    fun team(): TeamConfiguration {
        return teamConfiguration
    }

    @Get("/stadium")
    fun stadium(): StadiumConfiguration {
        return stadiumConfiguration
    }
}