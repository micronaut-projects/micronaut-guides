package example.micronaut

import io.micronaut.core.annotation.Introspected
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Positive

@Introspected // <1>
open class CommandBookSave {
    // <2>
    @NotBlank var title: String? = null

    // <3>
    @Positive var pages: Int = 0
}