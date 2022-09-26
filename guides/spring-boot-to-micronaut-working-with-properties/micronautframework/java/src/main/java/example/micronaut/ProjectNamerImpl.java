package example.micronaut;

import io.micronaut.context.annotation.Value;
import jakarta.inject.Singleton;

@Singleton
public class ProjectNamerImpl implements ProjectNamer {

    private final String prefix;
    private final String suffix;

    public ProjectNamerImpl(@Value("${project.prefix}") String prefix,
                            @Value("${project.suffix}") String suffix) {
        this.prefix = prefix;
        this.suffix = suffix;
    }

    @Override
    public String decorate(String name) {
        return prefix + name + suffix;
    }
}
