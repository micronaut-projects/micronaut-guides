package example.micronaut

import io.micronaut.context.env.Environment
import io.micronaut.runtime.Micronaut

class Application {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            Micronaut.build(*args)
                    .mainClass(Application::class.java)
                    .defaultEnvironments(Environment.DEVELOPMENT)
                    .start()
        }
    }
}