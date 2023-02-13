package example.micronaut

import io.micronaut.context.ApplicationContextBuilder
import io.micronaut.context.ApplicationContextConfigurer
import io.micronaut.context.annotation.ContextConfigurer
import io.micronaut.runtime.Micronaut.run
@ContextConfigurer
class Configurer: ApplicationContextConfigurer {
    override fun configure(builder: ApplicationContextBuilder) {
        //https://github.com/micronaut-projects/micronaut-test/issues/715
        //builder.eagerInitSingletons(true);
	}
}
fun main(args: Array<String>) {
	run(*args)
}

