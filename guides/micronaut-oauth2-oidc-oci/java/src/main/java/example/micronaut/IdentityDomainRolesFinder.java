package example.micronaut;

import io.micronaut.context.annotation.Replaces;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.core.util.StringUtils;
import io.micronaut.json.JsonMapper;
import io.micronaut.security.token.RolesFinder;
import io.micronaut.security.token.config.TokenConfiguration;
import jakarta.inject.Singleton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Singleton // <1>
@Replaces(RolesFinder.class)
public class IdentityDomainRolesFinder implements RolesFinder {
    private static final String KEY_NAME = "name";
    private final TokenConfiguration tokenConfiguration;

    public IdentityDomainRolesFinder(TokenConfiguration tokenConfiguration,
                                     JsonMapper jsonMapper) {
        this.tokenConfiguration = tokenConfiguration;
    }

    @Override
    public @NonNull List<String> resolveRoles(@Nullable Map<String, Object> attributes) {
        if (!attributes.containsKey(tokenConfiguration.getRolesName())) {
            return Collections.emptyList();
        }
        Object obj = attributes.get(tokenConfiguration.getRolesName());
        if (obj == null) {
            return Collections.emptyList();
        }
        if (obj instanceof Iterable objIterable) {
            List<String> roles = new ArrayList<>();
            for(Object o : objIterable) {
                if (o instanceof Map m) {
                    Object name = m.get(KEY_NAME);
                    if (name != null) {
                        String nameValue = name.toString();
                        if (StringUtils.isNotEmpty(nameValue)) {
                            roles.add(nameValue);
                        }
                    }
                }
            }
            return roles;
        }
        return Collections.emptyList();
    }
}
