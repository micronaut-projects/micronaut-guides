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

import com.mongodb.reactivestreams.client.MongoClient
import com.mongodb.reactivestreams.client.MongoCollection
import groovy.transform.CompileStatic
import io.micronaut.core.annotation.NonNull
import jakarta.inject.Singleton
import org.reactivestreams.Publisher
import reactor.core.publisher.Mono

import jakarta.validation.Valid
import jakarta.validation.constraints.NotNull

@CompileStatic
@Singleton // <1>
class MongoDbFruitRepository implements FruitRepository {

    private final MongoDbConfiguration mongoConf
    private final MongoClient mongoClient

    MongoDbFruitRepository(MongoDbConfiguration mongoConf,  // <2>
                           MongoClient mongoClient) {  // <3>
        this.mongoConf = mongoConf
        this.mongoClient = mongoClient
    }

    @Override
    Mono<Boolean> save(@NonNull @NotNull @Valid Fruit fruit) {
        Mono.from(collection.insertOne(fruit)) // <4>
                .map(insertOneResult -> true)
                .onErrorReturn(false)
    }

    @Override
    @NonNull
    Publisher<Fruit> list() {
        collection.find() // <4>
    }

    @NonNull
    private MongoCollection<Fruit> getCollection() {
        mongoClient.getDatabase(mongoConf.name)
                .getCollection(mongoConf.collection, Fruit)
    }
}
