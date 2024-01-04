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

import example.micronaut.domain.Thing
import example.micronaut.repository.ThingRepository
import groovy.transform.CompileStatic
import io.micronaut.context.annotation.Requires
import io.micronaut.context.event.StartupEvent
import io.micronaut.runtime.event.annotation.EventListener
import jakarta.inject.Singleton

import jakarta.transaction.Transactional

@Singleton
@Requires(notEnv = 'test')
@CompileStatic
class DataPopulator {

    private final ThingRepository thingRepository

    DataPopulator(ThingRepository thingRepository) {
        this.thingRepository = thingRepository
    }

    @EventListener
    @Transactional
    void init(StartupEvent event) {
        // clear out any existing data
        thingRepository.deleteAll()

        // create data
        Thing fred = new Thing('Fred')
        Thing barney = new Thing('Barney')
        thingRepository.saveAll([fred, barney])
    }
}
