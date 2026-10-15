from abc import ABC, abstractmethod

class ApiKeyRepository(ABC):  # <1>
    @abstractmethod
    def find_by_api_key(self, api_key: str) -> str | None:
        pass
