package example.micronaut

import io.micronaut.context.ApplicationContext
import io.micronaut.crac.test.CheckpointSimulator
import io.micronaut.http.client.BlockingHttpClient
import io.micronaut.http.client.HttpClient
import io.micronaut.runtime.server.EmbeddedServer
import spock.lang.Specification

class HelloControllerSpec extends Specification {

    void "test hello endpoint works after checkpoint"() throws IOException {
        given:
        EmbeddedServer server = ApplicationContext.run(EmbeddedServer)
        BlockingHttpClient client = createHttpClient(server)

        when:
        String body = client.retrieve("/")

        then:
        body == '{"message":"Hello World"}'

        when:
        CheckpointSimulator checkpointSimulator = server.applicationContext.getBean(CheckpointSimulator)
        checkpointSimulator.runBeforeCheckpoint()

        then:
        !server.running

        when:
        client.close()
        checkpointSimulator.runAfterRestore()

        then:
        server.running

        when:
        client = createHttpClient(server)

        body = client.retrieve("/")

        then:
        body == '{"message":"Hello World"}'

        cleanup:
        client.close()
    }

    private static BlockingHttpClient createHttpClient(EmbeddedServer embeddedServer) {
        String url = "http://localhost:${embeddedServer.port}"
        return embeddedServer.applicationContext.createBean(HttpClient, url).toBlocking();
    }
}