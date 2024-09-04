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
package example.micronaut;

import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Produces;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.annotation.ExecuteOn;

@Controller("/pygal") // <1>
class PygalController {

    private final PygalModule pygal; // <2>

    PygalController(PygalModule pygal) {
        this.pygal = pygal;
    }

    @ExecuteOn(TaskExecutors.BLOCKING)
    @Get // <4>
    @Produces("image/svg+xml") // <5>
    public String index() {
        PygalModule.StackedBar stackedBar = pygal.StackedBar(); // <6>
        stackedBar.add("Fibonacci", new int[] {0, 1, 1, 2, 3, 5, 8}); // <7>
        PygalModule.Svg svg = stackedBar.render(); // <8>
        return svg.decode(); // <9>
    }

}
