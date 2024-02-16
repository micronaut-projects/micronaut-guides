/*
 * Copyright 2017-2024 original authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package example.micronaut.entities;

import io.micronaut.data.annotation.Embeddable;
import io.micronaut.data.annotation.Relation;

import java.util.Objects;

@Embeddable // <1>
public class UserRoleId {

    @Relation(value = Relation.Kind.MANY_TO_ONE) // <2>
    private final User user;

    @Relation(value = Relation.Kind.MANY_TO_ONE) // <2>
    private final Role role;

    public UserRoleId(User user, Role role) {
        this.user = user;
        this.role = role;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UserRoleId userRoleId = (UserRoleId) o;
        return role.id().equals(userRoleId.getRole().id()) &&
                user.id().equals(userRoleId.getUser().id());
    }

    @Override
    public int hashCode() {
        return Objects.hash(role.id(), user.id());
    }

    public User getUser() {
        return user;
    }

    public Role getRole() {
        return role;
    }
}
