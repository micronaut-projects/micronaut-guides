from jakarta.inject import Singleton
from micronaut.context.annotation import Requires

from example.micronaut.username_fetcher import UsernameFetcher


# tag::clazz[]
@Requires(env="test")
@Singleton
class UserEchoClientReplacement(UsernameFetcher):

    async def find_username(self) -> str:
        return "sherlock"
# end::clazz[]
