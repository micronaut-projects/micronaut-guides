package example.micronaut

import io.micronaut.context.event.ApplicationEventListener
import io.micronaut.runtime.server.event.ServerStartupEvent
import javax.inject.Singleton

@Singleton // <1>
class BootStrap(val registerUseCase: RegisterUseCase) // <2>
    : ApplicationEventListener<ServerStartupEvent> { // <3>

    override fun onApplicationEvent(event: ServerStartupEvent) {  // <4>
        try {
            registerUseCase.register("harry@micronaut.example")
            Thread.sleep(20000)
            registerUseCase.register("ron@micronaut.example")
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }
}
