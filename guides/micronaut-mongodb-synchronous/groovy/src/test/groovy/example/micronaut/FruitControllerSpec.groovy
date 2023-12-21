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

import io.micronaut.http.HttpStatus
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import spock.lang.Specification

import static io.micronaut.http.HttpStatus.CREATED

@MicronautTest
class FruitControllerSpec extends Specification {

    @Inject
    FruitClient fruitClient

    void fruitsEndpointInteractsWithMongo() {

        when:
        List<Fruit> fruits = fruitClient.findAll()

        then:
        !fruits

        when:
        HttpStatus status = fruitClient.save(new Fruit('banana'))

        then:
        CREATED == status

        when:
        fruits = fruitClient.findAll()

        then:
        fruits
        'banana' == fruits[0].name
        null == fruits[0].description

        when:
        status = fruitClient.save(new Fruit('Apple', 'Keeps the doctor away'))

        then:
        CREATED == status

        when:
        fruits = fruitClient.findAll()

        then:
        fruits.find { it.description == 'Keeps the doctor away' }
    }
}
