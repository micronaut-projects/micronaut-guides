package example.micronaut

import example.micronaut.E164Utils.isValid
import io.micronaut.context.annotation.Factory
import io.micronaut.validation.validator.constraints.ConstraintValidator
import jakarta.inject.Singleton

@Factory // <1>
class CustomValidationFactory {

    @Singleton // <2>
    fun e164Validator(): ConstraintValidator<E164, String> {
        return ConstraintValidator { value, _, _ -> isValid(value) }
    }
}