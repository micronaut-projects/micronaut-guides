package example.micronaut;
import io.micronaut.context.annotation.Factory;
import io.micronaut.validation.validator.constraints.ConstraintValidator;
import jakarta.inject.Singleton;

@Factory // <1>
class CustomValidationFactory {
    /**
     * @return A {@link ConstraintValidator} implementation of a {@link E164} constraint for type {@link String}.
     */
    @Singleton // <2>
    ConstraintValidator<E164, String> e164Validator() {
        return (value, annotationMetadata, context) -> E164Utils.isValid(value);
    }
}