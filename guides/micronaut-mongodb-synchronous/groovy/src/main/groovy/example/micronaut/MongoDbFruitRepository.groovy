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
