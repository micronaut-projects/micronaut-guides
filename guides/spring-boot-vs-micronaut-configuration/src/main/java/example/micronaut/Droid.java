package example.micronaut;

import io.micronaut.serde.annotation.Serdeable;

@Serdeable
public class Droid {

    private String id;
    private String description;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
