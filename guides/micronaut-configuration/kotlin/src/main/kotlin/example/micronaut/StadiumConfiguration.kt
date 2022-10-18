package example.micronaut

import io.micronaut.context.annotation.EachProperty
import io.micronaut.context.annotation.Parameter
import io.micronaut.serde.annotation.Serdeable

@Serdeable // <1>
@EachProperty("stadium") // <2>
class StadiumConfiguration
constructor(@param:Parameter val name: String) {  // <3>
    var city: String? = null
    var size: Int? = null
}