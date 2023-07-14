package example.micronaut;

import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoCollection;
import io.micronaut.core.annotation.NonNull;
import jakarta.inject.Singleton;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Mono;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

@Singleton // <1>
public class MongoDbFruitRepository implements FruitRepository {

    private final MongoDbConfiguration mongoConf;
    private final MongoClient mongoClient;

    public MongoDbFruitRepository(MongoDbConfiguration mongoConf,  // <2>
                                  MongoClient mongoClient) {  // <3>
        this.mongoConf = mongoConf;
        this.mongoClient = mongoClient;
    }

    @Override
    public Mono<Boolean> save(@NonNull @NotNull @Valid Fruit fruit) {
        return Mono.from(getCollection().insertOne(fruit)) // <4>
                .map(insertOneResult -> true)
                .onErrorReturn(false);
    }

    @Override
    @NonNull
    public Publisher<Fruit> list() {
        return getCollection().find(); // <4>
    }

    @NonNull
    private MongoCollection<Fruit> getCollection() {
        return mongoClient.getDatabase(mongoConf.getName())
                .getCollection(mongoConf.getCollection(), Fruit.class);
    }
}
