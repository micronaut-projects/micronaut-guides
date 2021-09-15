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
import java.util.List;
import java.util.Optional;

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
        getCollection().insertOne(fruitToDocument(fruit));
    }

    @Override
    @NonNull
    public List<Fruit> list() {
        List<Fruit> list = new ArrayList<>();
        try (MongoCursor<Document> cursor = getCollection().find().iterator()) {
            while (cursor.hasNext()) {
                documentToFruit(cursor.next()).ifPresent(list::add);
            }
        }
        return list;
    }

    @NonNull
    private Optional<Fruit> documentToFruit(@NonNull Document document) {
        try {
            String name = document.getString("name");
            if (name == null) {
                return Optional.empty();
            }
            return Optional.of(new Fruit(name, document.getString("description")));
        } catch (ClassCastException e) {
            return Optional.empty();
        }
    }

    @NonNull
    private Document fruitToDocument(@NonNull Fruit fruit) {
        Document doc = new Document().append("name", fruit.getName());
        if (fruit.getDescription() != null) {
            return doc.append("description", fruit.getDescription());    
        }        
        return doc;
    }

    @NonNull
    private MongoCollection<Document> getCollection(){
        return mongoClient.getDatabase(mongoConf.getName())
                .getCollection(mongoConf.getCollection());
    }
}
