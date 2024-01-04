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

import io.micronaut.context.annotation.Requires;
import io.micronaut.context.env.Environment;
import io.micronaut.context.event.ApplicationEventListener;
import io.micronaut.context.event.StartupEvent;
import jakarta.inject.Singleton;

@Requires(env = Environment.DEVELOPMENT)
@Singleton
public class DevBootstrap implements ApplicationEventListener<StartupEvent> {

    private final IdGenerator idGenerator;
    private final BookRepository bookRepository;

    public DevBootstrap(IdGenerator idGenerator,
                     BookRepository bookRepository) {
        this.idGenerator = idGenerator;
        this.bookRepository = bookRepository;
    }

    @Override
    public void onApplicationEvent(StartupEvent event) {
        if (!bookRepository.existsTable()) {
            bookRepository.createTable();
            seedData();
        }
    }

    private void seedData(){
        bookRepository.save(new Book(idGenerator.generate(), "1680502395", "Release It!", 0));
        bookRepository.save(new Book(idGenerator.generate(), "0321601912", "Continuous Delivery", null));
        bookRepository.save(new Book(idGenerator.generate(), "1491950358", "Building Microservices", 4));
    }
}
