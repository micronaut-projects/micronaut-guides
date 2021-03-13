package example.micronaut

import io.micronaut.runtime.startApplication // <1>

object Application { // <2>
	@JvmStatic
	fun main(args: Array<String>) {
		startApplication<Application>(*args) // <3>
	}
}

