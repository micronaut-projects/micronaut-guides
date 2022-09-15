package example.micronaut.prototype;

import io.micronaut.context.annotation.Prototype;
import io.micronaut.core.annotation.NonNull;

import java.util.UUID;

@Prototype // <1>
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
