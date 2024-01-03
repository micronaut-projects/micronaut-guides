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
package example.micronaut;

import io.micronaut.context.event.ApplicationEventListener;
import io.micronaut.runtime.Micronaut;
import io.micronaut.runtime.server.event.ServerStartupEvent;

import jakarta.inject.Singleton;

@Singleton // <1>
public class Application implements ApplicationEventListener<ServerStartupEvent> {  // <2>

    private final RegisterUseCase registerUseCase;

    public Application(RegisterUseCase registerUseCase) {  // <3>
        this.registerUseCase = registerUseCase;
    }

    public static void main(String[] args) {
        Micronaut.run(Application.class);
    }

    @Override
    public void onApplicationEvent(ServerStartupEvent event) {  // <4>
        try {
            registerUseCase.register("harry@micronaut.example");
            Thread.sleep(20000);
            registerUseCase.register("ron@micronaut.example");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}