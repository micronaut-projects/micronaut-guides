package example.micronaut

import io.micronaut.core.annotation.Introspected
import io.micronaut.core.annotation.NonNull
import io.micronaut.core.annotation.Nullable
import javax.validation.constraints.NotEmpty

@Introspected // <1>
data class FruitCommand(
    val name: @NotEmpty String, // <2>
    val description: String? // <3>
)