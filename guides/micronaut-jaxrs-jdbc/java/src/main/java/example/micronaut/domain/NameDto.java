package example.micronaut.domain;

import io.micronaut.core.annotation.Introspected;

@Introspected
public class NameDto {

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}