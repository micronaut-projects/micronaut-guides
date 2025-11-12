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

import example.micronaut.UserState;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.data.annotation.GeneratedValue;
import io.micronaut.data.annotation.Id;
import io.micronaut.data.annotation.MappedEntity;
import jakarta.validation.constraints.NotBlank; // <4>

@MappedEntity // <1>
public record User(@Nullable
                   @Id
                   @GeneratedValue
                   Long id, // <2>
                   @NotBlank
                   String username, // <3>
                   @NotBlank
                   String password, // <3>
                   boolean enabled,
                   boolean accountExpired,
                   boolean accountLocked,
                   boolean passwordExpired) implements UserState {

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public boolean isAccountExpired() {
        return accountExpired;
    }

    @Override
    public boolean isAccountLocked() {
        return accountLocked;
    }

    @Override
    public boolean isPasswordExpired() {
        return false;
    }
}
