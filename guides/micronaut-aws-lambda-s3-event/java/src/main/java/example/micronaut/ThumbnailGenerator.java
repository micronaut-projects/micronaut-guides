package example.micronaut;

import io.micronaut.core.annotation.NonNull;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import java.io.InputStream;
import java.util.Optional;

public interface ThumbnailGenerator {

    @NonNull
    Optional<byte[]> thumbnail(@NonNull InputStream inputStream,
                               @NonNull @NotBlank @Pattern(regexp = "jpg|png") String format);
}
