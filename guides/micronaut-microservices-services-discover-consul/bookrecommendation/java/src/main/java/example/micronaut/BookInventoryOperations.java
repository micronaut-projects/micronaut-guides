package example.micronaut;

import io.micronaut.core.annotation.NonNull;
import io.reactivex.Maybe;
import javax.validation.constraints.NotBlank;

public interface BookInventoryOperations {
    Maybe<Boolean> stock(@NonNull @NotBlank String isbn);
}
