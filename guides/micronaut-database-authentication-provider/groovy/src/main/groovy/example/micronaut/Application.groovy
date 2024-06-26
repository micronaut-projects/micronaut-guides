/*
 * Copyright 2017-2024 original authors
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

import groovy.transform.CompileStatic
import io.micronaut.context.event.ApplicationEventListener
import io.micronaut.runtime.Micronaut
import io.micronaut.runtime.server.event.ServerStartupEvent
import jakarta.inject.Singleton

@CompileStatic
@Singleton
class Application implements ApplicationEventListener<ServerStartupEvent> { // <1>

    private final RegisterService registerService

    Application(RegisterService registerService) { // <2>
        this.registerService = registerService
    }

    @Override
    void onApplicationEvent(ServerStartupEvent event) { // <1>
        registerService.register('sherlock@micronaut.example', 'sherlock', 'elementary', ['ROLE_DETECTIVE']) // <3>
    }

    static void main(String[] args) {
        Micronaut.run Application, args
    }
}
