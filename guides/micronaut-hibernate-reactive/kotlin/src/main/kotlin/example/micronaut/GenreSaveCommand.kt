package example.micronaut

import io.micronaut.core.annotation.Introspected
import javax.validation.constraints.NotBlank

@Introspected // <1>
class GenreSaveCommand(@field:NotBlank var name: String)