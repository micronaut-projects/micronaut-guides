package example.micronaut

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.micronaut.context.annotation.ConfigurationBuilder
import io.micronaut.context.annotation.ConfigurationProperties
import io.micronaut.serde.annotation.Serdeable

@Serdeable // <1>
@JsonIgnoreProperties("builder") // <2>
//tag::teamConfigClassNoBuilder[]
@ConfigurationProperties("team")
class TeamConfiguration {
    String name
    String color
    List<String> playerNames
//end::teamConfigClassNoBuilder[]

    @ConfigurationBuilder(prefixes = "with", configurationPrefix = "team-admin") // <3>
    TeamAdmin.Builder builder = TeamAdmin.builder() // <4>
}

//tag::gettersandsetters[]
//end::gettersandsetters[]
