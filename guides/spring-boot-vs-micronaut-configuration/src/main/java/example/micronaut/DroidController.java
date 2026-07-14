package example.micronaut;

import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;

@Controller("/droid")
class DroidController {

    private final Droid droid;

    DroidController(Droid droid) {
        this.droid = droid;
    }

    @Get
    Droid getDroid() {
        return droid;
    }
}
