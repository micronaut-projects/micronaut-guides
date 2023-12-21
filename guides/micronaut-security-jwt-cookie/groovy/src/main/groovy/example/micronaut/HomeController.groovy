/*
 * Copyright 2017-2023 original authors
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
package example.micronaut

import groovy.transform.CompileStatic
import io.micronaut.core.annotation.Nullable
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.security.annotation.Secured
import io.micronaut.security.rules.SecurityRule
import io.micronaut.views.View

import java.security.Principal

@CompileStatic
@Secured(SecurityRule.IS_ANONYMOUS) // <1>
@Controller // <2>
class HomeController {

    @Get // <3>
    @View("home") // <4>
    Map<String, Object> index(@Nullable Principal principal) { // <5>
        Map<String, Object> data = [loggedIn: principal != null] as Map
        if (principal) {
            data.username = principal.name
        }
        data
    }
}
