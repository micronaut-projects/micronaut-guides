package example.micronaut;

import io.micronaut.context.annotation.DefaultImplementation;

import javax.validation.constraints.NotBlank;

@DefaultImplementation(HelloRepositoryDefault.class)
public interface HelloRepository {

    String findHelloByLanguage(@NotBlank  String language);

    void putHelloInLanguage(@NotBlank  String language, @NotBlank String hello);
}
