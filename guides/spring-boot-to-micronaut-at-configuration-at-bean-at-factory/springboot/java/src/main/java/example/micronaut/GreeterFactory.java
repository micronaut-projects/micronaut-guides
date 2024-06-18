package example.micronaut;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class GreeterFactory {

    @Bean
    Greeter helloGreeter() {
        return new HelloGreeter();
    }
}
