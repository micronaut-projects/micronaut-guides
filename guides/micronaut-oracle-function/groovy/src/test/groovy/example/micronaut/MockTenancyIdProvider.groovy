package example.micronaut

import groovy.transform.CompileStatic
import io.micronaut.context.annotation.Context
import io.micronaut.context.annotation.Replaces
import io.micronaut.oraclecloud.core.TenancyIdProvider
import jakarta.inject.Singleton

@CompileStatic
@Context
@Singleton
@Replaces(value = TenancyIdProvider)
class MockTenancyIdProvider implements TenancyIdProvider {

    final String tenancyId = 'id'
}
