package example.micronaut;

import edu.umd.cs.findbugs.annotations.NonNull;

import javax.validation.constraints.NotNull;

public interface ApplicationConfiguration {

    @NonNull
    Integer getMax();
}
