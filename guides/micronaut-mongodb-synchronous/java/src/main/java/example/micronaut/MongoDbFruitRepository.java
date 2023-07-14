package example.micronaut;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import io.micronaut.core.annotation.NonNull;
import jakarta.inject.Singleton;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

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
    public void save(@NonNull @NotNull @Valid Fruit fruit) {
        getCollection().insertOne(fruit);
    }

    @Override
    @NonNull
    public List<Fruit> list() {
        return getCollection().find().into(new ArrayList<>());
    }

    @NonNull
    private MongoCollection<Fruit> getCollection() {
        return mongoClient.getDatabase(mongoConf.getName())
                .getCollection(mongoConf.getCollection(), Fruit.class);
    }
}
