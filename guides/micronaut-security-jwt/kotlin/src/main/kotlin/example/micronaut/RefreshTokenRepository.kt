package example.micronaut

import io.micronaut.data.jdbc.annotation.JdbcRepository
import io.micronaut.data.model.query.builder.sql.Dialect.H2
import io.micronaut.data.repository.CrudRepository
import java.util.Optional
import jakarta.transaction.Transactional
import jakarta.validation.constraints.NotBlank

@JdbcRepository(dialect = H2) // <1>
interface RefreshTokenRepository : CrudRepository<RefreshTokenEntity, Long> { // <2>

    @Transactional
    fun save(@NotBlank username: String,
             @NotBlank refreshToken: String,
             revoked: Boolean): RefreshTokenEntity // <3>

    fun findByRefreshToken(@NotBlank refreshToken: String): Optional<RefreshTokenEntity> // <4>

    fun updateByUsername(@NotBlank username: String,
                         revoked: Boolean): Long // <5>
}
