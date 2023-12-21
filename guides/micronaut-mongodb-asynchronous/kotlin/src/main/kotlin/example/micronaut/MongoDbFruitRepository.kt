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

import com.mongodb.reactivestreams.client.MongoClient
import com.mongodb.reactivestreams.client.MongoCollection
import io.micronaut.core.annotation.NonNull
import jakarta.inject.Singleton
import org.reactivestreams.Publisher
import reactor.core.publisher.Mono
import jakarta.validation.Valid

@Singleton // <1>
open class MongoDbFruitRepository(
    private val mongoConf: MongoDbConfiguration,  // <2>
    private val mongoClient: MongoClient) : FruitRepository { // <3>

    override fun save(@Valid fruit: Fruit): Mono<Boolean> =
        Mono.from(collection.insertOne(fruit)) // <4>
                .map { true }
                .onErrorReturn(false)

    @NonNull
    override fun list(): Publisher<Fruit> = collection.find() // <4>

    private val collection: MongoCollection<Fruit>
        get() = mongoClient.getDatabase(mongoConf.name)
                .getCollection(mongoConf.collection, Fruit::class.java)
}
