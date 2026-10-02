from abc import ABC, abstractmethod


# tag::clazz[]
class UsernameFetcher(ABC):
    @abstractmethod
    async def find_username(self) -> str:
        ...
# end::clazz[]
