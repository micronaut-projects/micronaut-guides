package example.micronaut.domain

import io.micronaut.data.annotation.EmbeddedId
import io.micronaut.data.annotation.MappedEntity
import io.micronaut.serde.annotation.Serdeable

@Serdeable // <1>
@MappedEntity // <2>
class UserRole  {
    @EmbeddedId // <3>
    private UserRoleId userRoleId

    UserRole(UserRoleId userRoleId) {
        this.userRoleId = userRoleId
    }

    UserRoleId getUserRoleId() {
        return userRoleId
    }
}
