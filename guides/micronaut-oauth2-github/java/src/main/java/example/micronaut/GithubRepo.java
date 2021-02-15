package example.micronaut;

import io.micronaut.core.annotation.Introspected;

@Introspected
public class GithubRepo {

    private String name;

    public GithubRepo() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
