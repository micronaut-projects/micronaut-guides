package example.micronaut;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The annotated element must be a E.164 phone number.
 *
 * @see <a href="https://www.itu.int/rec/T-REC-E.164/en">ITU E.164 recommendation</a>
 * @see <a href="https://www.twilio.com/docs/glossary/what-e164">E.614</a>
 */
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.PARAMETER, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(E164.List.class)
@Documented
@Constraint(validatedBy = {})
public @interface E164 {

    String MESSAGE = "example.micronaut.E164.message";

    /**
     * @return message The error message
     */
    String message() default "{" + MESSAGE + "}";

    /**
     * @return Groups to control the order in which constraints are evaluated,
     * or to perform validation of the partial state of a JavaBean.
     */
    Class<?>[] groups() default {};

    /**
     * @return Payloads used by validation clients to associate some metadata information with a given constraint declaration
     */
    Class<? extends Payload>[] payload() default {};

    /**
     * List annotation.
     */
    @Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.PARAMETER, ElementType.TYPE_USE})
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @interface List {

        /**
         * @return An array of E164.
         */
        E164[] value();
    }
}
