package example.micronaut;

import io.micronaut.core.annotation.NonNull;

import java.math.BigDecimal;

public interface Vat {
    @NonNull
    String getCountry();

    @NonNull
    BigDecimal getRate();
}
