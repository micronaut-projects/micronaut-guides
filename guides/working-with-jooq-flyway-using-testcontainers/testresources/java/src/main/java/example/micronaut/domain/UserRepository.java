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
package example.micronaut.domain;

import static example.micronaut.jooq.tables.Users.USERS;
import static org.jooq.Records.mapping;

import java.time.LocalDateTime;
import java.util.Optional;

import jakarta.inject.Singleton;
import org.jooq.DSLContext;

@Singleton // <1>
class UserRepository {

    private final DSLContext dsl;

    UserRepository(DSLContext dsl) { // <2>
        this.dsl = dsl;
    }

    public User createUser(User user) {
        return this.dsl
                .insertInto(USERS)
                .set(USERS.NAME, user.name())
                .set(USERS.EMAIL, user.email())
                .set(USERS.CREATED_AT, LocalDateTime.now())
                .returningResult(USERS.ID, USERS.NAME, USERS.EMAIL)
                .fetchOne(mapping(User::new));
    }

    public Optional<User> getUserByEmail(String email) {
        return this.dsl
                .select(USERS.ID, USERS.NAME, USERS.EMAIL)
                .from(USERS)
                .where(USERS.EMAIL.equalIgnoreCase(email))
                .fetchOptional(mapping(User::new));
    }

    public void deleteUser(Long id) {
        dsl.deleteFrom(USERS).where(USERS.ID.eq(id)).execute();
    }
}
