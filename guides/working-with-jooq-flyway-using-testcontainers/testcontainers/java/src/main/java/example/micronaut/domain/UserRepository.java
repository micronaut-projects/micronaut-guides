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
