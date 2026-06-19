/*
 * Copyright 2017-2026 original authors
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
package example.micronaut.api;

import example.micronaut.services.EmployeeService;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.security.authentication.Authentication;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.annotation.ExecuteOn;
import io.micronaut.views.View;

import java.util.Map;

@Controller // <1>
public class HomeController {

    private final EmployeeService employeeService;

    public HomeController(EmployeeService employeeService) { // <2>
        this.employeeService = employeeService;
    }

    @Secured(SecurityRule.IS_ANONYMOUS) // <3>
    @View("home") // <4>
    @Get // <5>
    @ExecuteOn(TaskExecutors.BLOCKING) // <6>
    public Map<String, Object> index(@Nullable Authentication authentication) { // <7>
        return employeeService.employeeModel(authentication);
    }


}
