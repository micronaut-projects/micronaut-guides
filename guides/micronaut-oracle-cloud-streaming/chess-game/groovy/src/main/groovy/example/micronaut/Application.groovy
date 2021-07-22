package example.micronaut

import groovy.transform.CompileStatic
import io.micronaut.runtime.Micronaut

import static io.micronaut.context.env.Environment.DEVELOPMENT

@CompileStatic
class Application {

    static void main(String[] args) {
        Micronaut.build(args)
                .mainClass(Application)
                .defaultEnvironments(DEVELOPMENT)
                .start() 
    }
}
