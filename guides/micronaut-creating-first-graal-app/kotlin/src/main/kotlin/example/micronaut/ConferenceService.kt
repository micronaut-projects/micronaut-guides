package example.micronaut

import java.util.Random
import jakarta.inject.Singleton

@Singleton // <1>
class ConferenceService {

    fun randomConf(): Conference = CONFERENCES[Random().nextInt(CONFERENCES.size)] // <2>

    companion object {
        private val CONFERENCES = listOf(
                Conference("Greach"),
                Conference("GR8Conf EU"),
                Conference("Micronaut Summit"),
                Conference("Devoxx Belgium"),
                Conference("Oracle Code One"),
                Conference("CommitConf"),
                Conference("Codemotion Madrid")
        )
    }
}
