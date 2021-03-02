package example.micronaut;

import io.micronaut.core.annotation.Introspected;

@Introspected
public class BintrayPackage {

    private String name;
    private boolean linked;

    public BintrayPackage() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isLinked() {
        return linked;
    }

    public void setLinked(boolean linked) {
        this.linked = linked;
    }
}
