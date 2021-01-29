package example.micronaut;

import edu.umd.cs.findbugs.annotations.NonNull;
import io.reactivex.Maybe;
import javax.validation.constraints.NotBlank;

public interface BookInventoryOperations {
    Maybe<Boolean> stock(@NonNull @NotBlank String isbn);
}
