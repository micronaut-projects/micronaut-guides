package example.micronaut.singleton;

import io.micronaut.core.annotation.NonNull;
import jakarta.inject.Singleton;

@Singleton // <1>
public class RobotFather {
    private final Robot robot;

    public RobotFather(Robot robot) { // <2>
        this.robot = robot;
    }

    @NonNull
    public Robot child() {
        return this.robot;
    }
}
