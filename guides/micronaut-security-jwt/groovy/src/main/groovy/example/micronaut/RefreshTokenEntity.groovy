//tag::clazzwithoutsettersandgetters[]
package example.micronaut

import io.micronaut.core.annotation.NonNull
import io.micronaut.data.annotation.DateCreated
import io.micronaut.data.annotation.GeneratedValue
import io.micronaut.data.annotation.Id
import io.micronaut.data.annotation.MappedEntity

import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import java.time.Instant

@MappedEntity // <1>
class RefreshTokenEntity {
    @Id // <2>
    @GeneratedValue // <3>
    @NonNull
    Long id

    @NonNull
    @NotBlank
    String username

    @NonNull
    @NotBlank
    String refreshToken

    @NonNull
    @NotNull
    Boolean revoked

    @DateCreated // <4>
    @NonNull
    @NotNull
    Instant dateCreated
    //end::clazzwithoutsettersandgetters[]
//tag::endclass[]
}
//end::endclass[]
