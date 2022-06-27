package example.micronaut;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component // <1>
@ConfigurationProperties("greeting") // <2>
public class GreetingConfiguration {

    private String template = "Hello, %s!";

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }
}