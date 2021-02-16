package example.micronaut;

import edu.umd.cs.findbugs.annotations.NonNull;

public interface ApplicationConfiguration {

    @NonNull
    String getHostedDomain();
}
