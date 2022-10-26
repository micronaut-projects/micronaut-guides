package example.micronaut

import io.micronaut.serde.annotation.Serdeable
import javax.validation.constraints.Pattern
import javax.validation.constraints.Positive
import javax.validation.constraints.PositiveOrZero

@Serdeable
class SortingAndOrderArguments {
    @field:PositiveOrZero var offset:  Int? = null // <1>
    @field:Positive var max: Int? = null // <1>
    @field:Pattern(regexp = "id|name") var sort: String? = null // <1>
    @field:Pattern(regexp = "asc|ASC|desc|DESC") var order: String? = null // <1>
}