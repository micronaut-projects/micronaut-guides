package example.micronaut;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
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
