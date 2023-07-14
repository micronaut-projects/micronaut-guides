//tag::clazzwithoutsettersandgetters[]
package example.micronaut

import groovy.transform.CompileStatic
import io.micronaut.core.annotation.NonNull
import io.micronaut.data.annotation.DateCreated
import io.micronaut.data.annotation.GeneratedValue
import io.micronaut.data.annotation.Id
import io.micronaut.data.annotation.MappedEntity

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import java.time.Instant

@CompileStatic
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
