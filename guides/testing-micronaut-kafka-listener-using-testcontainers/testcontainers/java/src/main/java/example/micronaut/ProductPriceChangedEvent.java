package example.micronaut;

import io.micronaut.serde.annotation.Serdeable;

import java.math.BigDecimal;

@Serdeable // <1>
public record ProductPriceChangedEvent(String productCode, BigDecimal price) {}
