package example.micronaut

import io.micronaut.context.annotation.Requires
import io.micronaut.context.env.Environment
import io.micronaut.retry.annotation.Fallback
import io.reactivex.Maybe
import javax.inject.Singleton
import javax.validation.constraints.NotBlank

@Requires(env = arrayOf(Environment.TEST)) // <1>
@Fallback
@Singleton
open class BookInventoryClientStub : BookInventoryOperations {

    override fun stock(@NotBlank isbn: String): Maybe<Boolean> {
        if (isbn == "1491950358") {
            return Maybe.just(java.lang.Boolean.TRUE) // <2>

        } else if (isbn == "1680502395") {
            return Maybe.just(java.lang.Boolean.FALSE) // <3>
        }
        return Maybe.empty() // <4>
    }
}
