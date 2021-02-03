//tag::teamConfigClassNoBuilder[]
package example.micronaut

import io.micronaut.context.annotation.ConfigurationBuilder
import io.micronaut.context.annotation.ConfigurationProperties

@ConfigurationProperties("team")
class TeamConfiguration  {
    var name: String? = null
    var color: String? = null
    var playerNames: List<String>? = null
//end::teamConfigClassNoBuilder[]
//tag::teamConfigClassBuilder[]
    @ConfigurationBuilder(prefixes = ["with"], configurationPrefix = "team-admin") // <1>
    var builder = TeamAdmin.Builder() // <2>
//end::teamConfigClassBuilder[]
}