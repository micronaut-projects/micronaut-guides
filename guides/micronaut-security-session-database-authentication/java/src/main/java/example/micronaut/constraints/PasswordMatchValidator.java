package example.micronaut.constraints;

import example.micronaut.controllers.SignUpForm;
import io.micronaut.core.annotation.Introspected;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

@Introspected // <2>
public class PasswordMatchValidator implements ConstraintValidator<PasswordMatch, SignUpForm> {
    @Override
    public boolean isValid(SignUpForm value, ConstraintValidatorContext context) {
        if (value.password() == null && value.repeatPassword() == null) {
            return true;
        }
        if (value.password() != null && value.repeatPassword() == null) {
            return false;
        }
        if (value.password() == null) {
            return false;
        }
        return value.password().equals(value.repeatPassword());
    }
}
