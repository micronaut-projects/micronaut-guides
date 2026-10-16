/*
 * Copyright 2017-2026 original authors
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

import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Consumes;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.Produces;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.annotation.ExecuteOn;

@Controller("/library") // <1>
class LibraryController {

    private final LibraryAssistant assistant;

    LibraryController(LibraryAssistant assistant) { // <2>
        this.assistant = assistant;
    }

    @Post // <3>
    @Consumes(MediaType.TEXT_PLAIN) // <4>
    @Produces(MediaType.TEXT_PLAIN) // <5>
    @ExecuteOn(TaskExecutors.BLOCKING) // <6>
    String chat(@Body String message) {
        return assistant.chat(message);
    }
}
