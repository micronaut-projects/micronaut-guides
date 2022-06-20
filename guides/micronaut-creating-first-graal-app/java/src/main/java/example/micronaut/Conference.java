package example.micronaut;

import io.micronaut.core.annotation.Introspected;

@Introspected // <1>
public class Conference {

    private final String name;

    public Conference(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
