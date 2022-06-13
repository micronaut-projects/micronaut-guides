package example.micronaut;

import io.micronaut.runtime.Micronaut;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication // <1>
public class Application {

    public static void main(String... args) {
        Micronaut.run(Application.class);
    }
}
