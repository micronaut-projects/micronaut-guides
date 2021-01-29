package example.micronaut;

import edu.umd.cs.findbugs.annotations.NonNull;
import io.micronaut.context.annotation.Requires;
import io.micronaut.context.env.Environment;
import io.micronaut.retry.annotation.Fallback;
import io.reactivex.Maybe;

import javax.inject.Singleton;
import javax.validation.constraints.NotBlank;

@Requires(env = Environment.TEST) // <1>
@Fallback
@Singleton
public class BookInventoryClientStub implements BookInventoryOperations {

    @Override
    public Maybe<Boolean> stock(@NonNull @NotBlank String isbn) {
        if (isbn.equals("1491950358")) {
            return Maybe.just(Boolean.TRUE); // <2>

        } else if (isbn.equals("1680502395")) {
            return Maybe.just(Boolean.FALSE); // <3>
        }
        return Maybe.empty(); // <4>
    }
}
