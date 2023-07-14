package example.micronaut

import com.mongodb.client.MongoClient
import com.mongodb.client.MongoCollection
import jakarta.inject.Singleton
import jakarta.validation.Valid

@Singleton // <1>
open class MongoDbFruitRepository(
    private val mongoConf: MongoDbConfiguration,  // <2>
    private val mongoClient: MongoClient) : FruitRepository { // <3>

    override fun save(@Valid fruit: Fruit) {
        collection.insertOne(fruit)
    }

    override fun list(): List<Fruit> = collection.find().into(ArrayList())

    private val collection: MongoCollection<Fruit>
        get() = mongoClient.getDatabase(mongoConf.name)
                .getCollection(mongoConf.collection, Fruit::class.java)
}
