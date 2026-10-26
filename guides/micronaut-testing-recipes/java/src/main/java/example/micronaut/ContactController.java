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
package example.micronaut;

import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Produces;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import io.micronaut.views.View;

import java.util.Map;

@Controller("/contacts") // <1>
class ContactController {

    private final ContactRepository contactRepository;

    ContactController(ContactRepository contactRepository) { // <2>
        this.contactRepository = contactRepository;
    }

    @Secured(SecurityRule.IS_ANONYMOUS) // <3>
    @Produces(MediaType.TEXT_HTML) // <4>
    @View("contacts/list") // <5>
    @Get // <6>
    Map<String, Object> list() {
        return Map.of("contacts", contactRepository.findAll());
    }
}
