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
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.scheduling.TaskExecutors
import io.micronaut.scheduling.annotation.ExecuteOn
import jakarta.validation.constraints.NotBlank

@Controller("/things")
@ExecuteOn(TaskExecutors.BLOCKING)
class ThingController(private val thingRepository: ThingRepository) {

    @Get
    fun all(): List<Thing> = thingRepository.findAll()

    @Get("/{name}")
    fun byName(name: @NotBlank String?) = thingRepository.findByName(name)
}
