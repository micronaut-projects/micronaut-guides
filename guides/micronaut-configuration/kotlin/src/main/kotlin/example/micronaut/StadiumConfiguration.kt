package example.micronaut

import io.micronaut.context.annotation.EachProperty
import io.micronaut.context.annotation.Parameter
import io.micronaut.core.annotation.Introspected

@Introspected
@EachProperty("stadium") // <1>
class StadiumConfiguration
constructor(@param:Parameter val name: String) {  // <2>
    var city: String? = null
    var size: Int? = null
}