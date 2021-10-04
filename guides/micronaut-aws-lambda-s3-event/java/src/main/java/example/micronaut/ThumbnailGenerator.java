package example.micronaut;

import io.micronaut.core.annotation.NonNull;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.io.InputStream;
import java.util.Optional;

public interface ThumbnailGenerator {

    @NonNull
    Optional<byte[]> thumbnail(@NonNull InputStream inputStream,
                               @NonNull @NotBlank @Pattern(regexp = "jpg|png") String format);
}
