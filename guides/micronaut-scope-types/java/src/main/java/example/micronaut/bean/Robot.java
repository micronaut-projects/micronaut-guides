package example.micronaut.bean;

import io.micronaut.context.annotation.Bean;
import io.micronaut.core.annotation.NonNull;

import java.util.UUID;

@Bean
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
