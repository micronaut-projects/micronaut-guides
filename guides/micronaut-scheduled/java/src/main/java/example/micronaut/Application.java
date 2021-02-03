package example.micronaut;

import io.micronaut.context.event.ApplicationEventListener;
import io.micronaut.runtime.Micronaut;
import io.micronaut.runtime.server.event.ServerStartupEvent;

import javax.inject.Singleton;

@Singleton // <1>
public class Application implements ApplicationEventListener<ServerStartupEvent> {  // <2>

    private final RegisterUseCase registerUseCase;

    public Application(RegisterUseCase registerUseCase) {  // <3>
        this.registerUseCase = registerUseCase;
    }

    public static void main(String[] args) {
        Micronaut.run(Application.class);
    }


    @Override
    public void onApplicationEvent(ServerStartupEvent event) {  // <4>
        try {
            registerUseCase.register("harry@micronaut.example");
            Thread.sleep(20000);
            registerUseCase.register("ron@micronaut.example");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}