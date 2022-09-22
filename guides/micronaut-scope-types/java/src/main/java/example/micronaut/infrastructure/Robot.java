package example.micronaut.infrastructure;

import io.micronaut.context.annotation.Infrastructure;
import io.micronaut.core.annotation.NonNull;

import java.util.UUID;

@Infrastructure
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
