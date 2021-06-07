package example.micronaut

import groovy.transform.CompileStatic
import io.micronaut.core.annotation.ReflectiveAccess
import io.micronaut.oraclecloud.core.TenancyIdProvider
import io.micronaut.oraclecloud.function.OciFunction

import javax.inject.Inject
import javax.inject.Singleton

@CompileStatic
@Singleton
class Function extends OciFunction {

    @Inject
    TenancyIdProvider tenantIdProvider

    @ReflectiveAccess
    String handleRequest() {
        String tenancyId = tenantIdProvider.tenancyId
        return "Your tenancy is: $tenancyId"
    }
}
