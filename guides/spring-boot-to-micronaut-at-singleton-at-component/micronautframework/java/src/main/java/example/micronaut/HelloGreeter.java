package example.micronaut;

import jakarta.inject.Singleton;

@Singleton // <1>
public class HelloGreeter implements Greeter {
    @Override
    public String greet() {
        return "Hello";
    }
}
