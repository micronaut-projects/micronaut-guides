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

import io.micronaut.context.ApplicationContext;
import io.micronaut.inject.qualifiers.Qualifiers;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StadiumConfigurationTest {

    @Test
    void testStadiumConfiguration() {
        Map<String, Object> items = new HashMap<>();
        items.put("stadium.fenway.city", "Boston"); // <1>
        items.put("stadium.fenway.size", 60000);
        items.put("stadium.wrigley.city", "Chicago"); // <1>
        items.put("stadium.wrigley.size", 45000);

        ApplicationContext ctx = ApplicationContext.run(items);

        // <2>
        StadiumConfiguration fenwayConfiguration = ctx.getBean(StadiumConfiguration.class, Qualifiers.byName("fenway"));
        StadiumConfiguration wrigleyConfiguration = ctx.getBean(StadiumConfiguration.class, Qualifiers.byName("wrigley"));

        assertEquals("fenway", fenwayConfiguration.getName());
        assertEquals(60000, fenwayConfiguration.getSize());
        assertEquals("wrigley", wrigleyConfiguration.getName());
        assertEquals(45000, wrigleyConfiguration.getSize());

        ctx.close();
    }
}
