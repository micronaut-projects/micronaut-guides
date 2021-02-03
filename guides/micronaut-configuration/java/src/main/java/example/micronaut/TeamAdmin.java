package example.micronaut;

public class TeamAdmin { // <1>

    private String manager;
    private String coach;
    private String president;

    // should use the builder pattern to create the object
    private TeamAdmin() {
    }

    public String getManager() {
        return manager;
    }

    public void setManager(String manager) {
        this.manager = manager;
    }

    public String getCoach() {
        return coach;
    }

    public void setCoach(String coach) {
        this.coach = coach;
    }

    public String getPresident() {
        return president;
    }

    public void setPresident(String president) {
        this.president = president;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder { // <2>
        private String manager;
        private String coach;
        private String president;

        // <3>
        public Builder withManager(String manager) {
            this.manager = manager;
            return this;
        }

        public Builder withCoach(String coach) {
            this.coach = coach;
            return this;
        }

        public Builder withPresident(String president) {
            this.president = president;
            return this;
        }

        public TeamAdmin build() { // <4>
            TeamAdmin teamAdmin = new TeamAdmin();
            teamAdmin.manager = this.manager;
            teamAdmin.coach = this.coach;
            teamAdmin.president = this.president;
            return teamAdmin;
        }

        public String getManager() {
            return manager;
        }

        public String getCoach() {
            return coach;
        }

        public String getPresident() {
            return president;
        }
    }
}
