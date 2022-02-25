package example.micronaut

import groovy.transform.CompileStatic
import org.testcontainers.containers.MongoDBContainer
import org.testcontainers.utility.DockerImageName

@CompileStatic
class MongoDbUtils {

    static MongoDBContainer mongoDBContainer

    static void startMongoDb() {
        if (!mongoDBContainer) {
            mongoDBContainer = new MongoDBContainer(DockerImageName.parse('mongo:4.0.10'))
                    .withExposedPorts(27017)
        }
        if (!mongoDBContainer.running) {
            mongoDBContainer.start()
        }
    }

    static String getMongoDbUri() {
        if (!mongoDBContainer?.running) {
            startMongoDb()
        }
        mongoDBContainer.replicaSetUrl
    }

    static void closeMongoDb() {
        mongoDBContainer?.close()
    }
}
