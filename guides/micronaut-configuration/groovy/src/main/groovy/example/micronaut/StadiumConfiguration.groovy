package example.micronaut


import io.micronaut.context.annotation.EachProperty
import io.micronaut.context.annotation.Parameter

@EachProperty("stadium") // <1>
class StadiumConfiguration {
    String name // <2>
    String city
    Integer size

    StadiumConfiguration(@Parameter String name) { // <2>
        this.name = name
    }
}
