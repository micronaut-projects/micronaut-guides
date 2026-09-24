from jakarta.inject import Singleton
from micronaut.http import HttpRequest
from micronaut.security.token.reader import TokenReader


# tag::clazz[]
@Singleton  # <1>
class ApiKeyTokenReader(TokenReader):  # <2>
    def findToken(self, request: HttpRequest):
        return request.getHeaders().findFirst("X-API-KEY")
# end::clazz[]
