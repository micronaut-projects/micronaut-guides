package example.micronaut;

import io.micronaut.core.annotation.NonNull;
import org.reactivestreams.Publisher;
import io.micronaut.core.async.annotation.SingleResult;
import javax.validation.constraints.NotBlank;

public interface BookInventoryOperations {
    @SingleResult
    Publisher<Boolean> stock(@NonNull @NotBlank String isbn);
}
