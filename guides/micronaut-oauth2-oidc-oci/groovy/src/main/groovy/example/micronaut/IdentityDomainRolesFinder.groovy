package example.micronaut

import io.micronaut.context.annotation.Replaces
import io.micronaut.core.annotation.NonNull
import io.micronaut.core.annotation.Nullable
import io.micronaut.core.util.StringUtils
import io.micronaut.json.JsonMapper
import io.micronaut.security.token.RolesFinder
import io.micronaut.security.token.config.TokenConfiguration
import jakarta.inject.Singleton

@Singleton // <1>
@Replaces(RolesFinder)
class IdentityDomainRolesFinder implements RolesFinder {

    private static final String KEY_NAME = 'name'

    private final TokenConfiguration tokenConfiguration

    IdentityDomainRolesFinder(TokenConfiguration tokenConfiguration,
                              JsonMapper jsonMapper) {
        this.tokenConfiguration = tokenConfiguration
    }

    @Override
    @NonNull
    List<String> resolveRoles(@Nullable Map<String, Object> attributes) {
        if (!attributes?.containsKey(tokenConfiguration.rolesName)) {
            return Collections.emptyList()
        }
        Object obj = attributes[tokenConfiguration.rolesName]
        if (obj == null) {
            return Collections.emptyList()
        }
        if (obj instanceof Iterable) {
            return obj.findResults { Object o ->
                if (o instanceof Map) {
                    Object name = o[KEY_NAME]
                    String nameValue = name?.toString()
                    if (StringUtils.isNotEmpty(nameValue)) {
                        return nameValue
                    }
                }
                null
            }
        }
        Collections.emptyList()
    }
}
