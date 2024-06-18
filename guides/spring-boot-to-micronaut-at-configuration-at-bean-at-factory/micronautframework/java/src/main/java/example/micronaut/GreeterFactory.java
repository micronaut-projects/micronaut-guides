package example.micronaut;

import io.micronaut.context.annotation.Factory;
import io.micronaut.context.annotation.Bean;

@Factory
class GreeterFactory {
    @Bean
    Greeter helloGreeter() {
        return new HelloGreeter();
    }
}
