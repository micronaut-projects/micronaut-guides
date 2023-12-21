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

import groovy.transform.CompileStatic
import io.micronaut.context.annotation.Requires
import io.micronaut.context.event.StartupEvent
import io.micronaut.runtime.event.annotation.EventListener
import jakarta.inject.Singleton

import jakarta.transaction.Transactional

import static io.micronaut.context.env.Environment.TEST

@CompileStatic
@Singleton // <1>
class DataPopulator {

    private final BookRepository bookRepository

    DataPopulator(BookRepository bookRepository) { // <2>
        this.bookRepository = bookRepository
    }

    @EventListener // <3>
    @Transactional // <4>
    void init(StartupEvent event) {
        if (bookRepository.count() == 0) {
            bookRepository.save(new Book('1491950358', 'Building Microservices'))
            bookRepository.save(new Book('1680502395', 'Release It!'))
            bookRepository.save(new Book('0321601912', 'Continuous Delivery'))
        }
    }
}
