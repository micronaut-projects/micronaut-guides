package example.micronaut.constraints;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = PasswordMatchValidator.class)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface PasswordMatch {
    String MESSAGE = "example.micronaut.constraints.PasswordMatch.message";
    String message() default "{" + MESSAGE + "}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
