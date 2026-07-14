package example.micronaut;

import io.micronaut.context.annotation.ConfigurationProperties;

@ConfigurationProperties("greeting")
public class Greeting {

    private String name;
    private String coffee;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCoffee() {
        return coffee;
    }

    public void setCoffee(String coffee) {
        this.coffee = coffee;
    }
}
