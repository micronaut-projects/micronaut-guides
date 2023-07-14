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
