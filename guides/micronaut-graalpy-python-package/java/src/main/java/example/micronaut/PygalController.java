package example.micronaut;

import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Produces;
import jakarta.inject.Inject;

@Controller("/pygal") // <1>
public class PygalController {

    private PygalModule pygal; // <2>

    @Inject // <3>
    public void inject(PygalModule pygal) {
        this.pygal = pygal;
    }

    @Get // <4>
    @Produces("image/svg+xml") // <5>
    public String index() {
        PygalModule.StackedBar stackedBar = pygal.StackedBar(); // <6>
        stackedBar.add("Fibonacci", new int[] {0, 1, 1, 2, 3, 5, 8}); // <7>
        PygalModule.Svg svg = stackedBar.render(); // <8>
        return svg.decode(); // <9>
    }

}
