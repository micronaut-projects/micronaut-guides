package example.micronaut

import io.micronaut.context.annotation.Context
import io.micronaut.context.annotation.Replaces
import io.micronaut.oraclecloud.core.TenancyIdProvider
import jakarta.inject.Singleton

@Context
@Singleton
@Replaces(value = TenancyIdProvider::class)
class MockTenancyIdProvider : TenancyIdProvider {

    override fun getTenancyId() = "id"
}
