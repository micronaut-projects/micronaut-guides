package example.micronaut;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.naming.Named;
import io.micronaut.core.order.Ordered;

import java.util.List;

public interface Route extends Named, Ordered {

    @NonNull
    String getUri();

    @NonNull
    List<? extends RoutePredicate> getPredicates();
}
