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

import com.mongodb.client.MongoClient
import com.mongodb.client.MongoCollection
import io.micronaut.core.annotation.NonNull
import jakarta.inject.Singleton

import jakarta.validation.Valid
import jakarta.validation.constraints.NotNull

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
    void save(@NonNull @NotNull @Valid Fruit fruit) {
        collection.insertOne(fruit)
    }

    @Override
    @NonNull
    List<Fruit> list() {
        collection.find().into([])
    }

    @NonNull
    private MongoCollection<Fruit> getCollection() {
        return mongoClient.getDatabase(mongoConf.name)
                .getCollection(mongoConf.collection, Fruit)
    }
}
