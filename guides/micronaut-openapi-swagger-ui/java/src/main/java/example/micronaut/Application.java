package example.micronaut;

import io.micronaut.runtime.Micronaut;
import io.swagger.v3.oas.annotations.*;
import io.swagger.v3.oas.annotations.info.*;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
    info = @Info(
            title = "micronaut-guides",
            version = "1.0"
    ), servers = @Server(url = "https://guides.micronaut.io")
) // <1>
public class Application {
    public static void main(String[] args) {
        Micronaut.run(Application.class, args);
    }
}