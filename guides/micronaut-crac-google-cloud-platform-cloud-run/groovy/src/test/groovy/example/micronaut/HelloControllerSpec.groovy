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
        HttpClient httpClient = createHttpClient(server)
        BlockingHttpClient client = httpClient.toBlocking()

        when:
        String body = client.retrieve("/")

        then:
        body
        "{\"message\":\"Hello World\"}" == body

        when:
        CheckpointSimulator checkpointSimulator = server.getApplicationContext().getBean(CheckpointSimulator)
        checkpointSimulator.runBeforeCheckpoint()

        then:
        !server.isRunning()

        when:
        client.close()
        httpClient.close()
        checkpointSimulator.runAfterRestore()

        then:
        server.isRunning()

        when:
        httpClient = createHttpClient(server)
        client = httpClient.toBlocking()
        body = client.retrieve("/")

        then:
        body
        "{\"message\":\"Hello World\"}" == body

        cleanup:
        client.close()
        httpClient.close()
    }

    private static HttpClient createHttpClient(EmbeddedServer embeddedServer) {
        String url = "http://localhost:${embeddedServer.getPort()}"
        return embeddedServer.applicationContext.createBean(HttpClient, url);
    }
}