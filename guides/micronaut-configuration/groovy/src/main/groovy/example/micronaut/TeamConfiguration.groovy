
package example.micronaut

import io.micronaut.context.annotation.ConfigurationBuilder
import io.micronaut.context.annotation.ConfigurationProperties

//tag::teamConfigClassBuilder[]
//tag::teamConfigClassNoBuilder[]
@ConfigurationProperties("team")
class TeamConfiguration {
    String name
    String color
    List<String> playerNames
//end::teamConfigClassNoBuilder[]

    @ConfigurationBuilder(prefixes = "with", configurationPrefix = "team-admin") // <1>
    TeamAdmin.Builder builder = TeamAdmin.builder() // <2>
}
//end::teamConfigClassBuilder[]
