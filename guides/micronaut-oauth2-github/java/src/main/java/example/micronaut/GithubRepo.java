package example.micronaut;

import io.micronaut.serde.annotation.Serdeable;

@Serdeable
public class GithubRepo {

    private final String name;

    public GithubRepo(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
