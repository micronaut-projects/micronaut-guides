package example.micronaut

import io.micronaut.data.jdbc.annotation.JdbcRepository
import io.micronaut.data.model.query.builder.sql.Dialect
import io.micronaut.data.repository.CrudRepository
import java.util.Optional
import javax.transaction.Transactional
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

@JdbcRepository(dialect = Dialect.H2) // <1>
interface RefreshTokenRepository : CrudRepository<RefreshTokenEntity?, Long?> { // <2>

    @Transactional
    fun save(username: @NotBlank String,
             refreshToken: @NotBlank String,
             revoked: @NotNull Boolean): RefreshTokenEntity? // <3>

    fun findByRefreshToken(refreshToken: @NotBlank String): Optional<RefreshTokenEntity> // <4>

    fun updateByUsername(username: @NotBlank String,
                         revoked: @NotNull Boolean): Long // <5>
}
