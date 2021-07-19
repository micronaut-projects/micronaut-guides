package example.micronaut;

import io.micronaut.core.annotation.Nullable;
import io.micronaut.context.annotation.Context;
import io.micronaut.context.annotation.Replaces;
import io.micronaut.oraclecloud.core.TenancyIdProvider;

import javax.inject.Singleton;

@Context
@Singleton
@Replaces(value = TenancyIdProvider.class)
public class MockTenancyIdProvider implements TenancyIdProvider {

    @Nullable
    @Override
    public String getTenancyId() {
        return "id";
    }
}
