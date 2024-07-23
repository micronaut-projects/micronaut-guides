package example.micronaut;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest // <1>
class GreeterTest {

    @Autowired // <2>
    Greeter greeter;

    @Test
    void helloGreeterIsInjectedAsBeanOfTypeGreeter() {
        assertNotNull(greeter);
        assertEquals("Hello", greeter.greet());
    }

}