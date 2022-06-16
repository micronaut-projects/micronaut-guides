package example.micronaut

import io.micronaut.runtime.Micronaut

class Application {
    static void main(String[] args) {
        Micronaut.run(Application.class, args)
    }
}
