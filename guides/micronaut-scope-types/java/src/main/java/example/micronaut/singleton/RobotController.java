package example.micronaut.singleton;

//tag::imports[]
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;

import java.util.Arrays;
import java.util.List;
//end::imports[]

/*
//tag::controller[]
@Controller
//end::controller[]
*/
@Controller("/singleton") // <1>
//tag::clazz[]
public class RobotController {

    private final RobotFather father;
    private final RobotMother mother;

    public RobotController(RobotFather father,  // <2>
                           RobotMother mother) {
        this.father = father;
        this.mother = mother;
    }

    @Get // <3>
    List<String> children() {
        return Arrays.asList(
                father.child().getSerialNumber(),
                mother.child().getSerialNumber()
        );
    }
}
//end::clazz[]