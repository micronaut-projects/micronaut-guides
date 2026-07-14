package example.micronaut;

@FunctionalInterface
public interface ProjectNamer {
    String decorate(String name);
}
