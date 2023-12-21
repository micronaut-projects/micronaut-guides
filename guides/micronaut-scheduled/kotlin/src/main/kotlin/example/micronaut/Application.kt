/*
 * Copyright 2017-2023 original authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package example.micronaut

import io.micronaut.context.event.ApplicationEventListener
import io.micronaut.runtime.Micronaut.build
import io.micronaut.runtime.server.event.ServerStartupEvent
import jakarta.inject.Singleton

fun main(args: Array<String>) {
    build()
        .args(*args)
        .packages("example.micronaut")
        .start()
}

@Singleton // <1>
class Application(val registerUseCase: RegisterUseCase) // <2>
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
