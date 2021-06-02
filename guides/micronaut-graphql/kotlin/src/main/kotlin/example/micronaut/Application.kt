package example.micronaut

import io.micronaut.context.env.Environment
import io.micronaut.runtime.Micronaut.*

fun main(args: Array<String>) {
    build()
        .args(*args)
        .packages("com.example")
        .defaultEnvironments(Environment.DEVELOPMENT) // <1>
        .start()
}

