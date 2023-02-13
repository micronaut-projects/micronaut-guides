package example.micronaut

import io.micronaut.core.annotation.NonNull
import io.micronaut.context.ApplicationContextBuilder
import io.micronaut.context.ApplicationContextConfigurer
import io.micronaut.context.annotation.ContextConfigurer
import io.micronaut.runtime.Micronaut
import groovy.transform.CompileStatic

@CompileStatic
class Application {

    @ContextConfigurer
    static class Configurer implements ApplicationContextConfigurer {
        @Override
        public void configure(@NonNull ApplicationContextBuilder builder) {
            //https://github.com/micronaut-projects/micronaut-test/issues/715
            //builder.eagerInitSingletons(true)
        }
    }
    static void main(String[] args) {
        Micronaut.run(Application, args)
    }
}
