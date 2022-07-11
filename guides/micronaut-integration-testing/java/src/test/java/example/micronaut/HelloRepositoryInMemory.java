package example.micronaut;

import io.micronaut.context.annotation.Replaces;
import io.micronaut.context.annotation.Requires;
import jakarta.inject.Singleton;

import java.util.HashMap;
import java.util.Map;

@Singleton
@Replaces(HelloRepositoryDefault.class) // <1>
@Requires(env = io.micronaut.context.env.Environment.TEST) // <2>
class HelloRepositoryInMemory implements HelloRepository {

    private final Map<String, String> helloMap;

    HelloRepositoryInMemory() {
        this.helloMap = new HashMap<>();
    }

    @Override
    public String findHelloByLanguage(String language) {
        return helloMap.getOrDefault(language, "Hello!");
    }

    @Override
    public void putHelloInLanguage(String language, String hello) {
        helloMap.put(language, hello);
    }

    public void clearRepository() {
        helloMap.clear();
    }
}
