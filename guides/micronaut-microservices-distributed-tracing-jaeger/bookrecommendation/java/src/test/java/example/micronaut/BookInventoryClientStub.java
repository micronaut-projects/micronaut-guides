package example.micronaut;

import io.micronaut.context.annotation.Requires;
import io.micronaut.context.env.Environment;
import io.micronaut.retry.annotation.Fallback;
import io.reactivex.Maybe;

import javax.inject.Singleton;
import javax.validation.constraints.NotBlank;

@Requires(env = Environment.TEST)
@Fallback
@Singleton
public class BookInventoryClientStub implements BookInventoryOperations {

    @Override
    public Maybe<Boolean> stock(@NotBlank String isbn) {
        if (isbn.equals("1491950358")) {
            return Maybe.just(Boolean.TRUE);

        } else if (isbn.equals("1680502395")) {
            return Maybe.just(Boolean.FALSE);
        }
        return Maybe.empty();
    }
}
