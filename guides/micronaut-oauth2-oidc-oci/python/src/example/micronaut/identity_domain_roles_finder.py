from collections.abc import Iterable

from jakarta.inject import Singleton
from micronaut.context.annotation import Replaces
from micronaut.security.token import RolesFinder
from micronaut.security.token.config import TokenConfiguration


@Singleton  # <1>
@Replaces(RolesFinder)
class IdentityDomainRolesFinder(RolesFinder):
    KEY_NAME = "name"

    def __init__(self, token_configuration: TokenConfiguration):
        self.token_configuration = token_configuration

    def resolveRoles(self, attributes) -> list[str]:
        roles_name = self.token_configuration.getRolesName()
        groups = self._get(attributes, roles_name)

        if groups is None or isinstance(groups, str) or not isinstance(groups, Iterable):
            return []

        roles = []
        for group in groups:
            name = self._get(group, self.KEY_NAME)
            if name is not None and str(name):
                roles.append(str(name))
        return roles

    @staticmethod
    def _get(values, key):
        if values is None:
            return None
        if hasattr(values, "containsKey"):
            return values.get(key) if values.containsKey(key) else None
        if isinstance(values, dict):
            return values.get(key)
        return None
