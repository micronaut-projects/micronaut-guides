package example.micronaut

import jakarta.inject.Singleton

@Singleton // <1>
class HelloRepositoryDefault implements HelloRepository {

    @Override
    String findHelloByLanguage(String language) {
        return "Could not hello :("
    }

    @Override
    void putHelloInLanguage(String language, String hello) {
    }

}
