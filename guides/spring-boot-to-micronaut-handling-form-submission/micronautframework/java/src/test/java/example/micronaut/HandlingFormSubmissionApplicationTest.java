package example.micronaut;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.MediaType;
import io.micronaut.http.client.BlockingHttpClient;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertTrue;

@MicronautTest
class HandlingFormSubmissionApplicationTest {
	@Test
	void rendersForm(@Client("/") HttpClient httpClient)  {
		BlockingHttpClient client = httpClient.toBlocking();
		assertTrue(client.retrieve("/greeting").contains("Form"));
	}
	@Test
	void submitsForm(@Client("/") HttpClient httpClient)  {
		BlockingHttpClient client = httpClient.toBlocking();
		String html = client.retrieve(HttpRequest.POST("/greeting",
						Map.of("id", "12345", "content", "Hello"))
				.contentType(MediaType.APPLICATION_FORM_URLENCODED_TYPE));
		assertTrue(html.contains("Result"));
		assertTrue(html.contains("id: 12345"));
	}
}
