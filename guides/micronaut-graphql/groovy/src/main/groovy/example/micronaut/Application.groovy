package example.micronaut

import io.micronaut.runtime.Micronaut
import groovy.transform.CompileStatic
import io.micronaut.context.env.Environment

@CompileStatic
class Application {
    static void main(String[] args) {
        Micronaut.build(args)
                .mainClass(Application.class)
                .defaultEnvironments(Environment.DEVELOPMENT) // <1>
                .start()
    }
}