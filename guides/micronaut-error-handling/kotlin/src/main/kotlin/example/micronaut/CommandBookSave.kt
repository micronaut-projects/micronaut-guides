package example.micronaut

import io.micronaut.core.annotation.Introspected
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Positive

@Introspected // <1>
data class CommandBookSave(
    @NotBlank // <2>
    var title: String? = null,

    @Positive // <3>
    var pages: Int
)
