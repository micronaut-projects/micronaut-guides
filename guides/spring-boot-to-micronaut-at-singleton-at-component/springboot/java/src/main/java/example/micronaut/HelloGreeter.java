package example.micronaut;
import org.springframework.stereotype.Component;

@Component // <1>
public class HelloGreeter implements Greeter {
    @Override
    public String greet() {
        return "Hello";
    }
}
