package example.micronaut

import io.micronaut.runtime.Micronaut.build
import io.micronaut.context.env.Environment.DEVELOPMENT

fun main(args: Array<String>) {
	build()
	    .args(*args)
		.packages("example.micronaut")
		.defaultEnvironments(DEVELOPMENT)
		.start()
}
