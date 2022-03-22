package example.micronaut;

import jakarta.inject.Inject;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import example.micronaut.domain.Owner;
import io.micronaut.core.type.Argument;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;

import java.util.List;

@MicronautTest
class OwnerControllerTest {

    @Inject
    @Client("/")
    HttpClient client;

    @Test
    void testListInitialOwners() {

        List<Owner> results = client.toBlocking().retrieve(HttpRequest.GET("/owners"), Argument.listOf(Owner.class));

        Assertions.assertEquals(
                2,
                results.size()
        );
    }
}