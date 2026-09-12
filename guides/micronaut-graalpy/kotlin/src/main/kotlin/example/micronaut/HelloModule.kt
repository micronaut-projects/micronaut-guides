package example.micronaut

import io.micronaut.graal.graalpy.annotations.GraalPyModule

@GraalPyModule("hello") // <1>
interface HelloModule {
    fun hello(txt: String): String // <2>
}
