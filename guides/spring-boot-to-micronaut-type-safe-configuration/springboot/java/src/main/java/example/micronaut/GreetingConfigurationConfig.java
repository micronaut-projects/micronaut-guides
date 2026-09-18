package example.micronaut;

import org.springframework.context.annotation.Configuration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@Configuration
@EnableConfigurationProperties({GreetingConfiguration.class})
public class GreetingConfigurationConfig {
}