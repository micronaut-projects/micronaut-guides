package example.micronaut

import io.micronaut.context.env.Environment.DEVELOPMENT
import io.micronaut.runtime.Micronaut.build

fun main(args: Array<String>) {
    build()
        .args(*args)
        .packages("example.micronaut")
        .defaultEnvironments(DEVELOPMENT)
        .start()
}
