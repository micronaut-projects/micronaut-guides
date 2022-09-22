package example.micronaut.beanfactory;

import io.micronaut.core.annotation.NonNull;

import java.util.UUID;

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
