package example.micronaut

import io.micronaut.context.annotation.Replaces
import io.micronaut.context.annotation.Requires
import io.micronaut.context.env.Environment
import jakarta.inject.Singleton

@Singleton
@Replaces(HelloRepositoryDefault.class) // <1>
@Requires(env = Environment.TEST) // <2>
class HelloRepositoryInMemory implements HelloRepository {

    private final Map<String, String> helloMap;

    HelloRepositoryInMemory() {
        this.helloMap = new HashMap<>()
    }

    @Override
    String findHelloByLanguage(String language) {
        return helloMap.getOrDefault(language, "Hello!")
    }

    @Override
    void putHelloInLanguage(String language, String hello) {
        helloMap.put(language, hello)
    }

    void clearRepository() {
        helloMap.clear()
    }
}
