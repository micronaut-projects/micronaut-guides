package example.micronaut;

import io.micronaut.core.annotation.Introspected;

@Introspected
public class GithubRepo {

    private final String name;

    public GithubRepo(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
