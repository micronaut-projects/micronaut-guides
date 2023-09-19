package example.micronaut.context

import io.micronaut.context.annotation.ConfigurationProperties
import io.micronaut.context.annotation.Context
import jakarta.validation.constraints.Pattern

@Context // <1>
@ConfigurationProperties("micronaut") // <2>
class MicronautConfiguration {

    @field:Pattern(regexp = "groovy|java|kotlin") // <3>
    var language: String? = null

}