package example.micronaut

import example.micronaut.clients.CourseClient
import example.micronaut.clients.StudentClient
import io.micronaut.context.ApplicationContext
import io.micronaut.runtime.EmbeddedApplication
import org.testcontainers.containers.MongoDBContainer
import org.testcontainers.utility.DockerImageName
import spock.lang.AutoCleanup
import spock.lang.Shared
import spock.lang.Specification

abstract class BaseMongoDataSpec extends Specification {

    static MongoDBContainer mongoDBContainer

    static void startMongoDb() {
        if (mongoDBContainer == null) {
            mongoDBContainer = new MongoDBContainer(DockerImageName.parse("mongo:5.0.6"))
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

    @Shared
    @AutoCleanup
    EmbeddedApplication<?> application

    def setupSpec() {
        startMongoDb()
        application = ApplicationContext.run(EmbeddedApplication, [
                'mongodb.uri': getMongoDbUri()
        ] + extraProperties)
    }

    protected Map<String, ?> getExtraProperties() {
        [:]
    }

    def cleanup() {
        mongoDBContainer.execInContainer("mongosh", "--eval", "db.dropDatabase()")
    }

    def cleanupSpec() {
        closeMongoDb()
    }

    CourseClient getCourseClient() {
        application.applicationContext.createBean(CourseClient, application.URL)
    }

    StudentClient getStudentClient() {
        application.applicationContext.createBean(StudentClient, application.URL)
    }
}