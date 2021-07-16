package example.micronaut;

import org.reactivestreams.Publisher;
import io.micronaut.core.async.annotation.SingleResult;
import javax.validation.constraints.NotBlank;

public interface BookInventoryOperations {
    @SingleResult
    Publisher<Boolean> stock(@NotBlank String isbn);
}
