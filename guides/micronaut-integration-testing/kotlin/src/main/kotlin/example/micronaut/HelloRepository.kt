package example.micronaut

import io.micronaut.context.annotation.DefaultImplementation
import javax.validation.constraints.NotBlank

@DefaultImplementation(HelloRepositoryDefault::class)
interface HelloRepository {

    fun findHelloByLanguage(@NotBlank language: String): String

    fun putHelloInLanguage(@NotBlank language: String, @NotBlank hello: String)
}