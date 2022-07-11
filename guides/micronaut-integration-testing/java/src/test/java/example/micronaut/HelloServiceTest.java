package example.micronaut;

import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@MicronautTest(startApplication=false) // <1>
class HelloServiceTest {

    @Inject // <2>
    HelloRepositoryInMemory helloRepository;

    @Inject // <2>
    HelloService helloService;

    @AfterEach
    void afterEach() {
        helloRepository.clearRepository();
    }

    @Test
    void shouldReturnHello_whenHelloIsInRepository() {
        //Given
        var polishHello = "Czesc!";
        helloRepository.putHelloInLanguage("polish", polishHello);

        //When
        String result = helloService.sayHello("polish");

        //Then
        assertEquals(result, polishHello); // <3>
    }

    @Test
    void shouldReturnDefaultHello_ifHelloNotInRepository() {
        //When
        String result = helloService.sayHello("polish");

        //Then
        assertEquals(result, "Hello!"); // <3>
    }
}
