package example.micronaut

import javax.validation.Constraint
import javax.validation.Payload
import java.lang.annotation.ElementType
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy
import java.lang.annotation.Target

@Constraint(validatedBy = [])
@Target(value = [ElementType.TYPE])
@Retention(RetentionPolicy.RUNTIME)
@interface EmailConstraints {

    String message() default "{email.invalid}"

    Class<?>[] groups() default []

    Class<? extends Payload>[] payload() default []

}
