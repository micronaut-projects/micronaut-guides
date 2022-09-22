package example.micronaut.request;

import io.micronaut.core.annotation.NonNull;
import jakarta.inject.Singleton;

@Singleton
public class RobotFather {
    private final Robot robot;

    public RobotFather(Robot robot) {
        this.robot = robot;
    }

    @NonNull
    public Robot child() {
        return this.robot;
    }
}
