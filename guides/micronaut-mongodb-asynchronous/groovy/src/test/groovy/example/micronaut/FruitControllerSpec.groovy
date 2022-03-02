package example.micronaut

import io.micronaut.http.HttpStatus
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import io.micronaut.test.support.TestPropertyProvider
import jakarta.inject.Inject
import spock.lang.Specification

import static io.micronaut.http.HttpStatus.CREATED

@MicronautTest
class FruitControllerSpec extends Specification implements TestPropertyProvider {

    @Inject
    FruitClient fruitClient

    void fruitsEndpointInteractsWithMongo() {

        when:
        List<Fruit> fruits = fruitClient.findAll()

        then:
        !fruits

        when:
        HttpStatus status = fruitClient.save(new Fruit('banana'))

        then:
        CREATED == status

        when:
        fruits = fruitClient.findAll()

        then:
        fruits
        'banana' == fruits[0].name
        null == fruits[0].description

        when:
        status = fruitClient.save(new Fruit('Apple', 'Keeps the doctor away'))

        then:
        CREATED == status

        when:
        fruits = fruitClient.findAll()

        then:
        fruits.find { it.description == 'Keeps the doctor away' }
    }

    void cleanupSpec() {
        MongoDbUtils.closeMongoDb()
    }

    @Override
    Map<String, String> getProperties() {
        MongoDbUtils.startMongoDb()
        ['mongodb.uri': MongoDbUtils.mongoDbUri]
    }
}
