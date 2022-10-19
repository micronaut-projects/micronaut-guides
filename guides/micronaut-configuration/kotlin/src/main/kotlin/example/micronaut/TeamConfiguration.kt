package example.micronaut

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.micronaut.context.annotation.ConfigurationBuilder
import io.micronaut.context.annotation.ConfigurationProperties
import io.micronaut.serde.annotation.Serdeable

@Serdeable // <1>
@JsonIgnoreProperties("builder") // <2>
//tag::teamConfigClassNoBuilder[]
@ConfigurationProperties("team")
class TeamConfiguration  {
    var name: String? = null
    var color: String? = null
    var playerNames: List<String>? = null
//end::teamConfigClassNoBuilder[]
//tag::teamConfigClassBuilder[]
    @ConfigurationBuilder(prefixes = ["with"], configurationPrefix = "team-admin") // <3>
    var builder = TeamAdmin.Builder() // <4>
//end::teamConfigClassBuilder[]
}

//tag::gettersandsetters[]
//end::gettersandsetters[]