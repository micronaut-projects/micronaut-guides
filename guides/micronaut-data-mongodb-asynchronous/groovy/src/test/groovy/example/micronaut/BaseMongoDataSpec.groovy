package example.micronaut

import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.runtime.EmbeddedApplication
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import io.micronaut.test.support.TestPropertyProvider
import jakarta.inject.Inject
import org.testcontainers.containers.MongoDBContainer
import org.testcontainers.utility.DockerImageName
import spock.lang.AutoCleanup
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Stepwise

@MicronautTest
abstract class BaseMongoDataSpec extends Specification implements TestPropertyProvider {

    @Shared
    @AutoCleanup
    MongoDBContainer mongoDBContainer

    @Inject
    EmbeddedApplication application

    @Inject
    FruitClient fruitClient

    @Inject
    @Client("/")
    HttpClient httpClient

    void startMongoDb() {
        if (mongoDBContainer == null) {
            mongoDBContainer = new MongoDBContainer(DockerImageName.parse("mongo:5.0.6"))
                    .withExposedPorts(27017)
        }
        if (!mongoDBContainer.running) {
            mongoDBContainer.start()
        }
    }

    String getMongoDbUri() {
        if (!mongoDBContainer?.running) {
            startMongoDb()
        }
        mongoDBContainer.replicaSetUrl
    }

    @Override
    Map<String, String> getProperties() {
        ['mongodb.uri': mongoDbUri]
    }

    def cleanup() {
        // For non-stepwise tests, clear the database after each test
        if (!this.getClass().getAnnotation(Stepwise)) {
            mongoDBContainer.execInContainer("mongosh", "--eval", "db.dropDatabase()")
        }
    }
}