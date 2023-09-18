package example.micronaut;

import io.micronaut.context.BeanContext;
import io.micronaut.core.type.Argument;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import io.micronaut.transaction.TransactionOperations;
import jakarta.inject.Inject;
import org.hibernate.Session;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

@MicronautTest
public class TransactionOperationsTest {
    @Inject
    BeanContext beanContext;

    @Test
    void beanOfTypeTransactionOperations() {
        assertTrue(beanContext.containsBean(TransactionOperations.class));
        assertTrue(beanContext.findBean(Argument.of(TransactionOperations.class, Session.class)).isPresent());
    }
}
