from abc import ABC, abstractmethod


# tag::clazz[]
class ApiKeyRepository(ABC):  # <1>
    @abstractmethod
    def find_by_api_key(self, api_key: str) -> str | None:
        pass
# end::clazz[]
