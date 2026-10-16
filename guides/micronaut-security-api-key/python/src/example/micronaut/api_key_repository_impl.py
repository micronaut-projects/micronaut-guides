from jakarta.inject import Singleton

from .api_key_configuration import ApiKeyConfiguration
from .api_key_repository import ApiKeyRepository


# tag::clazz[]
@Singleton  # <1>
class ApiKeyRepositoryImpl(ApiKeyRepository):
    def __init__(self, api_keys: list[ApiKeyConfiguration]):
        self.keys = {configuration.key: configuration.name for configuration in api_keys}

    def find_by_api_key(self, api_key: str) -> str | None:
        return self.keys.get(api_key)
# end::clazz[]
