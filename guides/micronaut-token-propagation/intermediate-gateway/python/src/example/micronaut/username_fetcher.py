from abc import ABC, abstractmethod

from org.reactivestreams import Publisher


# tag::clazz[]
class UsernameFetcher(ABC):
    @abstractmethod
    def findUsername(self, authorization: str) -> Publisher[str]:
        ...
# end::clazz[]
