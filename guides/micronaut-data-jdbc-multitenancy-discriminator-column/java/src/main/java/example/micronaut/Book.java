package example.micronaut;

import io.micronaut.core.annotation.Nullable;
import io.micronaut.data.annotation.GeneratedValue;
import io.micronaut.data.annotation.Id;
import io.micronaut.data.annotation.MappedEntity;
import io.micronaut.data.annotation.TenantId;
import io.micronaut.serde.annotation.Serdeable;

@Serdeable // <1>
@MappedEntity // <2>
public record Book(@Nullable
                   @Id // <3>
                   @GeneratedValue // <4>
                   Long id,
                   String title,

                   @TenantId // <5>
                   String framework) {
}
