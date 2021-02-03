package example.micronaut

class TeamAdmin private constructor(
        val manager: String?,
        val coach: String?,
        val president: String?) { // <1>
    data class Builder( // <2>
            var manager: String? = null,
            var coach: String? = null,
            var president: String? = null) {
        fun withManager(manager: String) = apply { this.manager = manager } // <3>
        fun withCoach(coach: String) = apply { this.coach = coach }
        fun withPresident(president: String) = apply { this.president = president }
        fun build() = TeamAdmin(manager, coach, president) // <4>
    }
}