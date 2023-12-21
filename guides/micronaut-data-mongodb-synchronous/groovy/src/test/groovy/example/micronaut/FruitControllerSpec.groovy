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

import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import spock.lang.Specification

@MicronautTest
class FruitControllerSpec extends Specification {

    @Inject
    FruitClient fruitClient

    def "empty database contains no fruit"() {
        expect:
        fruitClient.list().empty
    }

    void "fruits endpoint interacts with mongodb"() {
        when:
        HttpResponse<Fruit> response = fruitClient.save(new Fruit('banana', null))

        then:
        response.status == HttpStatus.CREATED
        def banana = response.body.get()

        when:
        Iterable<Fruit> fruits = fruitClient.list()

        then:
        fruits*.name == ['banana']
        fruits*.description == [null]

        when:
        response = fruitClient.save(new Fruit('Apple', 'Keeps the doctor away'))

        then:
        response.status == HttpStatus.CREATED

        when:
        fruits = fruitClient.list()

        then:
        fruits.find { it.description == 'Keeps the doctor away' }

        when: 'we update the description'
        banana.description = 'Yellow and curved'
        fruitClient.update(banana)

        and: 'we get a list of known fruits'
        fruits = fruitClient.list()

        then: 'descriptions are updated'
        fruits*.description.toSet() == ['Keeps the doctor away', 'Yellow and curved'] as Set<String>
    }

    def "search works as expected"() {
        given:
        fruitClient.save(new Fruit('apple', 'Keeps the doctor away'))
        fruitClient.save(new Fruit('pineapple', 'Delicious'))
        fruitClient.save(new Fruit('lemon', 'Lemonentary my dear Dr Watson'))

        when:
        Iterable<Fruit> fruit = fruitClient.query(["apple", "pineapple"])

        then:
        fruit*.name.toSet() == ['apple', 'pineapple'] as Set<String>
    }

}
