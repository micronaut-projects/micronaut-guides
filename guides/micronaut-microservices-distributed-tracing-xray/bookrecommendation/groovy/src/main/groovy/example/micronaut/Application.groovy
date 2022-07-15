package example.micronaut

import io.micronaut.context.env.Environment
import io.micronaut.runtime.Micronaut

class Application {

    static void main(String[] args) {
        Micronaut.build(args)
                .mainClass(Application)
                .defaultEnvironments(Environment.DEVELOPMENT)
                .start()
    }
}
