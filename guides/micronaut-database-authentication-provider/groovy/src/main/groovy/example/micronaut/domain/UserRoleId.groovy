package example.micronaut.domain

import groovy.transform.EqualsAndHashCode
import io.micronaut.core.annotation.Creator
import jakarta.persistence.Embeddable
import io.micronaut.serde.annotation.Serdeable

@EqualsAndHashCode
@Serdeable // <1>
@Embeddable // <2>
class UserRoleId {
    final Long userId
    final Long roleId

    @Creator // <3>
    UserRoleId(Long userId,
               Long roleId) {
        this.userId = userId
        this.roleId = roleId
    }

    UserRoleId() {

    }
}
