from jakarta.inject import Singleton
from micronaut.context.annotation import Requires
from org.reactivestreams import Publisher
from reactor.core.publisher import Mono

from example.micronaut.username_fetcher import UsernameFetcher


# tag::clazz[]
@Requires(env="test")
@Singleton
class UserEchoClientReplacement(UsernameFetcher):

    def findUsername(self) -> Publisher[str]:
        return Mono.just("sherlock")
# end::clazz[]
