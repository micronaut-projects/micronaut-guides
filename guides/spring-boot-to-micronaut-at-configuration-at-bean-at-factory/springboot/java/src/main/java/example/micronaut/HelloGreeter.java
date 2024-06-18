package example.micronaut;

public class HelloGreeter implements Greeter {
    @Override
    public String greet() {
        return "Hello";
    }
}
