//tag::clazzwithoutsettersandgetters[]
package example.micronaut

import io.micronaut.data.annotation.DateCreated
import io.micronaut.data.annotation.GeneratedValue
import io.micronaut.data.annotation.Id
import io.micronaut.data.annotation.MappedEntity
import java.time.Instant
import jakarta.validation.constraints.NotBlank

@MappedEntity // <1>
data class RefreshTokenEntity(

    @field:Id // <2>
    @GeneratedValue // <3>
    var id: Long? = null,

    @NotBlank
    var username: String,

    @NotBlank
    var refreshToken: String,

    var revoked: Boolean,

    @DateCreated // <4>
    var dateCreated: Instant? = null
    //end::clazzwithoutsettersandgetters[]
//tag::endclass[]
)
//end::endclass[]
