package example.micronaut.singleton;
/*
//tag::pkg[]
package example.micronaut;
//end::pkg[]
*/
//tag::clazz[]
import io.micronaut.core.annotation.NonNull;
import jakarta.inject.Singleton;

import java.util.UUID;

@Singleton // <1>
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
//end::clazz[]
