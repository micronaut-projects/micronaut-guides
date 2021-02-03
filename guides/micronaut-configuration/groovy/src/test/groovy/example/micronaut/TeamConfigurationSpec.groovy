
package example.micronaut

import example.micronaut.TeamAdmin
import example.micronaut.TeamConfiguration
import io.micronaut.context.ApplicationContext
import spock.lang.Specification

class TeamConfigurationSpec extends Specification {

    //tag::teamConfigSpecNoBuilder[]
    void "test team configuration"() {
        given:
        ApplicationContext ctx = ApplicationContext.run(ApplicationContext, [ // <1>
                "team.name": 'evolution',
                "team.color": 'green',
                "team.player-names": ['Nirav Assar', 'Lionel Messi']
        ])

        when:
        TeamConfiguration teamConfiguration = ctx.getBean(TeamConfiguration)

        then:
        teamConfiguration.name == "evolution"
        teamConfiguration.color == "green"
        teamConfiguration.playerNames[0] == "Nirav Assar"
        teamConfiguration.playerNames[1] == "Lionel Messi"

        cleanup:
        ctx.close()
    }
    //end::teamConfigSpecNoBuilder[]

    void "test builder pattern plain usage"() {
        when:
        TeamAdmin teamAdmin = new TeamAdmin.Builder().withManager("Nirav")
                .withCoach("Tommy")
                .withPresident("Mark").build()

        then:
        teamAdmin.manager == "Nirav"
        teamAdmin.coach == "Tommy"
        teamAdmin.president == "Mark"
    }

    //tag::teamConfigSpecBuilder[]
    void "test team configuration admin configuration builder "() {
        given:
        ApplicationContext ctx = ApplicationContext.run(ApplicationContext, [
                "team.name": 'evolution',
                "team.color": 'green',
                "team.player-names": ['Nirav Assar', 'Lionel Messi'],
                "team.team-admin.manager": "Jerry Jones", // <1>
                "team.team-admin.coach": "Tommy O'Neill",
                "team.team-admin.president": "Mark Scanell"
        ])

        when:
        TeamConfiguration teamConfiguration = ctx.getBean(TeamConfiguration)
        TeamAdmin teamAdmin = teamConfiguration.builder.build() // <2>

        then:
        teamConfiguration.name == "evolution"
        teamConfiguration.color == "green"
        teamConfiguration.playerNames[0] == "Nirav Assar"
        teamConfiguration.playerNames[1] == "Lionel Messi"

        // check the builder has values set
        teamConfiguration.builder.manager == "Jerry Jones"
        teamConfiguration.builder.coach == "Tommy O'Neill"
        teamConfiguration.builder.president == "Mark Scanell"

        // check the object can be built
        teamAdmin.manager == "Jerry Jones" // <3>
        teamAdmin.coach == "Tommy O'Neill"
        teamAdmin.president == "Mark Scanell"

        cleanup:
        ctx.close()
    }
    //end::teamConfigSpecBuilder[]
}
