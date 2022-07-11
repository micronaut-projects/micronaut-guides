package example.micronaut;

import jakarta.inject.Singleton;

@Singleton // <1>
class HelloRepositoryDefault implements HelloRepository {

    HelloRepositoryDefault() {}

    @Override
    public String findHelloByLanguage(String language) {
        return "Could not hello :(";
    }

    @Override
    public void putHelloInLanguage(String language, String hello) {
    }
}
