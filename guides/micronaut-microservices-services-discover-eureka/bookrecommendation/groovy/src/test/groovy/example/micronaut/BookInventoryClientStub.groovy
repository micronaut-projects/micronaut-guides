package example.micronaut

import io.micronaut.context.annotation.Requires
import io.micronaut.context.env.Environment
import io.reactivex.Maybe
import io.micronaut.retry.annotation.Fallback
import javax.inject.Singleton
import javax.validation.constraints.NotBlank

@Requires(env = Environment.TEST) // <1>
@Fallback
@Singleton
class BookInventoryClientStub implements BookInventoryOperations {

    @Override
    Maybe<Boolean> stock(@NotBlank String isbn) {
        if (isbn == "1491950358") {
            return Maybe.just(Boolean.TRUE) // <2>

        } else if (isbn == "1680502395") {
            return Maybe.just(Boolean.FALSE) // <3>
        }
        Maybe.empty() // <4>
    }
}
