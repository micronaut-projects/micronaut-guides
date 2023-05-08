package example.micronaut;

import io.micronaut.context.annotation.ConfigurationProperties;
import io.micronaut.core.annotation.NonNull;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

@ConfigurationProperties("vat") // <1>
public class VatConfiguration implements Vat { // <2>
    @NonNull
    @NotNull
    private BigDecimal rate;

    @NotBlank
    @NotNull
    private String country;

    @Override
    @NonNull
    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(@NonNull BigDecimal rate) {
        this.rate = rate;
    }

    @Override
    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
