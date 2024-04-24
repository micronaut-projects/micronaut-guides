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

import example.micronaut.TeamAdmin;
import example.micronaut.TeamConfiguration;
import io.micronaut.context.ApplicationContext;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TeamConfigurationTest {

    //tag::teamConfigSpecNoBuilder[]
    @Test
    void testTeamConfiguration() {
        List<String> names = Arrays.asList("Nirav Assar", "Lionel Messi");
        Map<String, Object> items = new HashMap<>();
        items.put("team.name", "evolution");
        items.put("team.color", "green");
        items.put("team.player-names", names);

        ApplicationContext ctx = ApplicationContext.run(items); // <1>
        TeamConfiguration teamConfiguration = ctx.getBean(TeamConfiguration.class);

        assertEquals("evolution", teamConfiguration.getName());
        assertEquals("green", teamConfiguration.getColor());
        assertEquals(names.size(), teamConfiguration.getPlayerNames().size());
        names.forEach(name -> assertTrue(teamConfiguration.getPlayerNames().contains(name)));

        ctx.close();
    }
    //end::teamConfigSpecNoBuilder[]

    @Test
    void testBuilderPatternPlainUsage() {
        TeamAdmin teamAdmin = new TeamAdmin.Builder().withManager("Nirav")
                .withCoach("Tommy")
                .withPresident("Mark").build();

        assertEquals("Nirav", teamAdmin.getManager());
        assertEquals("Tommy", teamAdmin.getCoach());
        assertEquals("Mark", teamAdmin.getPresident());
    }

    //tag::teamConfigSpecBuilder[]
    @Test
    void testTeamConfigurationBuilder() {
        List<String> names = Arrays.asList("Nirav Assar", "Lionel Messi");
        Map<String, Object> items = new HashMap<>();
        items.put("team.name", "evolution");
        items.put("team.color", "green");
        items.put("team.team-admin.manager", "Jerry Jones"); // <1>
        items.put("team.team-admin.coach", "Tommy O'Neill");
        items.put("team.team-admin.president", "Mark Scanell");
        items.put("team.player-names", names);

        ApplicationContext ctx = ApplicationContext.run(items);
        TeamConfiguration teamConfiguration = ctx.getBean(TeamConfiguration.class);
        TeamAdmin teamAdmin = teamConfiguration.builder.build(); // <2>

        assertEquals("evolution", teamConfiguration.getName());
        assertEquals("green", teamConfiguration.getColor());
        assertEquals("Nirav Assar", teamConfiguration.getPlayerNames().get(0));
        assertEquals("Lionel Messi", teamConfiguration.getPlayerNames().get(1));

        // check the builder has values set
        assertEquals("Jerry Jones", teamConfiguration.builder.getManager());
        assertEquals("Tommy O'Neill", teamConfiguration.builder.getCoach());
        assertEquals("Mark Scanell", teamConfiguration.builder.getPresident());

        // check the object can be built
        assertEquals("Jerry Jones", teamAdmin.getManager()); // <3>
        assertEquals("Tommy O'Neill", teamAdmin.getCoach());
        assertEquals("Mark Scanell", teamAdmin.getPresident());

        ctx.close();
    }
    //end::teamConfigSpecBuilder[]
}
