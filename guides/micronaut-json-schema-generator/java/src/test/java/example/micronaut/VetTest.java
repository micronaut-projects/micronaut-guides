package example.micronaut;

import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import jakarta.inject.Inject;

@MicronautTest
class VetTest {

    @Inject
    VetClient client;

    @Test
    void testCat() {
        String inputData = """
            {
              "resourceType": "Cat",
              "id": "0x",
              "birthdate": "2000-01-01",
              "name": "Micronaut",
              "hasMate": true
            }
            """.replaceAll("\\s", "");
        var response = client.getAnimalType(inputData);
        Assertions.assertNotNull(response);
        Assertions.assertEquals(AnimalTypes.CAT, response);
    }
}
