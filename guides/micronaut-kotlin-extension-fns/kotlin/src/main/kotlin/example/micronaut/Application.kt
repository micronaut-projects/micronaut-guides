package example.micronaut

import io.micronaut.kotlin.runtime.startApplication // <1>

object ApplicationKt { // <2>
	@JvmStatic
	fun main(args: Array<String>) {
		startApplication<ApplicationKt>(*args) // <3>
	}
}

