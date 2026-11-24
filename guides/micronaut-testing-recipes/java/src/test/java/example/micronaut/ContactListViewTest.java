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

import io.micronaut.core.io.Writable;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import io.micronaut.views.thymeleaf.ThymeleafViewsRenderer;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertTrue;

@MicronautTest(startApplication = false) // <1>
class ContactListViewTest {

    @Inject
    ThymeleafViewsRenderer<Map<String, Object>> viewsRenderer; // <2>

    @Test
    void contactListRendersContacts() throws IOException {
        Map<String, Object> model = Map.of("contacts",
                List.of(new Contact("Tim", "Cook", "Apple Inc.")));
        Writable writable = viewsRenderer.render("contacts/list", model, null); // <3>
        String html = WritableUtils.writableToString(writable); // <4>
        assertTrue(html.contains("<!DOCTYPE html>"));
        assertTrue(html.contains("<td>Tim</td>"));
        assertTrue(html.contains("<td>Apple Inc.</td>"));
    }
}
