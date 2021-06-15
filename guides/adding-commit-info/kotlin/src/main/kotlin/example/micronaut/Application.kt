package example.micronaut

import io.micronaut.core.annotation.Introspected
import io.micronaut.runtime.Micronaut.*

@Introspected
class ApplicationKt {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            build()
                .args(*args)
                .packages("example.micronaut")
                .start()
        }
    }
}
