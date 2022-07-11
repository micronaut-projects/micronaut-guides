package example.micronaut

import jakarta.inject.Singleton

@Singleton // <1>
class HelloRepositoryDefault: HelloRepository {

    override fun findHelloByLanguage(language: String): String {
        return "Could not hello :("
    }

    override fun putHelloInLanguage(language: String, hello: String) {}
}