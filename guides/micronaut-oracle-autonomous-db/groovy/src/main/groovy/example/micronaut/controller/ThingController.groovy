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
package example.micronaut.controller

import example.micronaut.domain.Thing
import example.micronaut.repository.ThingRepository
import groovy.transform.CompileStatic
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.scheduling.TaskExecutors
import io.micronaut.scheduling.annotation.ExecuteOn
import jakarta.validation.constraints.NotBlank

@Controller('/things')
@ExecuteOn(TaskExecutors.BLOCKING)
@CompileStatic
class ThingController {

    private final ThingRepository thingRepository

    ThingController(ThingRepository thingRepository) {
        this.thingRepository = thingRepository
    }

    @Get
    List<Thing> all() {
        thingRepository.findAll()
    }

    @Get('/{name}')
    Optional<Thing> byName(@NotBlank String name) {
        thingRepository.findByName(name)
    }
}
