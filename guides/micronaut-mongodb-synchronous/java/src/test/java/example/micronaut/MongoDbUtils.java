package example.micronaut;

import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.utility.DockerImageName;

public class MongoDbUtils {

    static MongoDBContainer mongoDBContainer;

    public static void startMongoDb() {
        if (mongoDBContainer == null) {
            mongoDBContainer = new MongoDBContainer(DockerImageName.parse("mongo:4.0.10"))
                    .withExposedPorts(27017);
        }
        if (!mongoDBContainer.isRunning()) {
            mongoDBContainer.start();
        }
    }

    public static String getMongoDbUri() {
        if (mongoDBContainer == null || !mongoDBContainer.isRunning()) {
            startMongoDb();
        }
        return mongoDBContainer.getReplicaSetUrl();
    }

    public static void closeMongoDb() {
        if (mongoDBContainer != null) {
            mongoDBContainer.close();
        }
    }
}
