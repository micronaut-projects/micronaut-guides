/*
 * Copyright 2017-2024 original authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
//tag::pkg[]
package example.micronaut.singleton;
//end::pkg[]

//tag::imports[]
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;

import java.util.Arrays;
import java.util.List;
//end::imports[]

//tag::controller[]
@Controller("/singleton") // <1>
//end::controller[]

//tag::clazz[]
public class RobotController {

    private final RobotFather father;
    private final RobotMother mother;

    public RobotController(RobotFather father,  // <2>
                           RobotMother mother) { // <3>
        this.father = father;
        this.mother = mother;
    }

    @Get // <4>
    List<String> children() {
        return Arrays.asList(
                father.child().getSerialNumber(),
                mother.child().getSerialNumber()
        );
    }
}
//end::clazz[]