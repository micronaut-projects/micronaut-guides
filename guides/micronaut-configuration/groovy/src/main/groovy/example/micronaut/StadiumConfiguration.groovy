package example.micronaut

import io.micronaut.context.annotation.EachProperty
import io.micronaut.context.annotation.Parameter
import io.micronaut.serde.annotation.Serdeable

@Serdeable // <1>
@EachProperty("stadium") // <2>
class StadiumConfiguration {
    String name // <3>
    String city
    Integer size

    StadiumConfiguration(@Parameter String name) { // <3>
        this.name = name
    }

}
