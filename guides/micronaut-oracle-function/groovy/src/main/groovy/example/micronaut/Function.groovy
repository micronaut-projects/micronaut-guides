package example.micronaut

import groovy.transform.CompileStatic
import io.micronaut.core.annotation.ReflectiveAccess
import io.micronaut.oraclecloud.core.TenancyIdProvider
import io.micronaut.oraclecloud.function.OciFunction
import jakarta.inject.Inject
import jakarta.inject.Singleton

@CompileStatic
@Singleton
class Function extends OciFunction {

    @Inject
    TenancyIdProvider tenantIdProvider

    @ReflectiveAccess // <1>
    String handleRequest() {
        String tenancyId = tenantIdProvider.tenancyId
        return "Your tenancy is: $tenancyId"
    }
}
