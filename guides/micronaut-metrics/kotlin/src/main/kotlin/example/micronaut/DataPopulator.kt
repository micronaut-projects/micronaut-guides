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

import io.micronaut.context.annotation.Requires
import io.micronaut.context.env.Environment.TEST
import io.micronaut.context.event.StartupEvent
import io.micronaut.runtime.event.annotation.EventListener
import jakarta.inject.Singleton
import jakarta.transaction.Transactional

@Singleton // <1>
open class DataPopulator(private val bookRepository: BookRepository) { // <2>

    @EventListener // <3>
    @Transactional // <4>
    open fun init(event: StartupEvent) {
        if (bookRepository.count() == 0L) {
            bookRepository.save(Book("1491950358", "Building Microservices"))
            bookRepository.save(Book("1680502395", "Release It!"))
            bookRepository.save(Book("0321601912", "Continuous Delivery"))
        }
    }
}
