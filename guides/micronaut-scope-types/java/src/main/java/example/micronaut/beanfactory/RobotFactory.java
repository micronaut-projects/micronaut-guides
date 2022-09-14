package example.micronaut.beanfactory;

import io.micronaut.context.annotation.Bean;
import io.micronaut.context.annotation.Factory;

@Factory
public class RobotFactory {
    @Bean
    Robot create() {
        return new Robot();
    }
}
