package example.micronaut;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import io.micronaut.core.annotation.NonNull;
import jakarta.inject.Singleton;
import org.bson.Document;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

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
    public void save(@NonNull @NotNull @Valid Fruit fruit){
        getCollection().insertOne(fruit);
    }

    @Override
    @NonNull
    public Iterable<Fruit> list() {
        return getCollection().find();
    }
    
    @NonNull
    private MongoCollection<Fruit> getCollection(){
        return mongoClient.getDatabase(mongoConf.getName())
                .getCollection(mongoConf.getCollection(), Fruit.class);
    }
}
