package example.micronaut

import org.testcontainers.containers.MongoDBContainer
import org.testcontainers.utility.DockerImageName

object MongoDbUtils {

    var mongoDBContainer: MongoDBContainer? = null

    fun startMongoDb() {
        if (mongoDBContainer == null) {
            mongoDBContainer = MongoDBContainer(DockerImageName.parse("mongo:4.0.10"))
                    .withExposedPorts(27017)
        }
        if (!mongoDBContainer!!.isRunning) {
            mongoDBContainer!!.start()
        }
    }

    val mongoDbUri: String
        get() {
            if (mongoDBContainer == null || !mongoDBContainer!!.isRunning) {
                startMongoDb()
            }
            return mongoDBContainer!!.replicaSetUrl
        }

    fun closeMongoDb() {
        if (mongoDBContainer != null) {
            mongoDBContainer!!.close()
        }
    }
}
