package example.micronaut

import groovy.transform.CompileStatic

@CompileStatic
class TeamAdmin { // <1>

    String manager
    String coach
    String president

    // should use the builder pattern to create the object
    private TeamAdmin() {
    }

    static Builder builder() {
        return new Builder()
    }

    static class Builder { // <2>
        String manager
        String coach
        String president

        // <3>
        Builder withManager(String manager) {
            this.manager = manager
            this
        }

        Builder withCoach(String coach) {
            this.coach = coach
            this
        }

        Builder withPresident(String president) {
            this.president = president
            this
        }

        TeamAdmin build() { // <4>
            TeamAdmin teamAdmin = new TeamAdmin()
            teamAdmin.manager = this.manager
            teamAdmin.coach = this.coach
            teamAdmin.president = this.president
            teamAdmin
        }
    }
}
