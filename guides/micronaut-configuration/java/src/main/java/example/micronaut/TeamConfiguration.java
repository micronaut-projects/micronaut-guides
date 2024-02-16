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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.micronaut.context.annotation.ConfigurationBuilder;
import io.micronaut.context.annotation.ConfigurationProperties;
import io.micronaut.serde.annotation.Serdeable;

import java.util.List;

@Serdeable // <1>
@JsonIgnoreProperties("builder") // <2>
//tag::teamConfigClassNoBuilder[]
@ConfigurationProperties("team")
public class TeamConfiguration {
    private String name;
    private String color;
    private List<String> playerNames;
//end::teamConfigClassNoBuilder[]

    public TeamConfiguration() {
    }

    @ConfigurationBuilder(prefixes = "with", configurationPrefix = "team-admin") // <3>
    protected TeamAdmin.Builder builder = TeamAdmin.builder(); // <4>

    public TeamAdmin.Builder getBuilder() {
        return builder;
    }

    public void setBuilder(TeamAdmin.Builder builder) {
        this.builder = builder;
    }

    //tag::gettersandsetters[]
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public List<String> getPlayerNames() {
        return playerNames;
    }

    public void setPlayerNames(List<String> playerNames) {
        this.playerNames = playerNames;
    }
}
//end::gettersandsetters[]
