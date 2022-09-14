package example.micronaut.refreshable;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.runtime.context.scope.Refreshable;
import java.util.UUID;

@Refreshable
public class Robot {
    @NonNull
    private final String serialNumber;

    public Robot() {
        serialNumber = UUID.randomUUID().toString();
    }

    @NonNull
    public String getSerialNumber() {
        return serialNumber;
    }
}
