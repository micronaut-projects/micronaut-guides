package example.micronaut;

import io.micronaut.core.annotation.ReflectiveAccess;
import io.micronaut.oraclecloud.core.TenancyIdProvider;
import io.micronaut.oraclecloud.function.OciFunction;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Date;

@Singleton
public class Function extends OciFunction {

    @Inject
    TenancyIdProvider tenantIdProvider;

    @ReflectiveAccess // <1>
    public String handleRequest() {
        String tenancyId = tenantIdProvider.getTenancyId();
        return "Your tenancy is: " + tenancyId;
    }
}
