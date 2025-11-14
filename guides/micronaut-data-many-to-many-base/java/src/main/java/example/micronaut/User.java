package example.micronaut;

import io.micronaut.core.annotation.Introspected;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import java.util.List;

@Introspected // <1>
public record User(@NonNull @NotBlank Long id,
                   @NonNull @NotBlank String username,
                   @Nullable List<String> authorities) {
}
