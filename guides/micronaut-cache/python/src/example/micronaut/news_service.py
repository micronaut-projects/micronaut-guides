from time import sleep

from jakarta.inject import Singleton
from java.time import Month
from micronaut.cache.annotation import CacheConfig, CacheInvalidate, CachePut, Cacheable


@Singleton  # <1>
@CacheConfig("headlines")  # <2>
class NewsService:
    def __init__(self):
        self._headlines = {
            Month.NOVEMBER: [
                "Micronaut Graduates to Trial Level in Thoughtworks technology radar Vol.1",
                "Micronaut AOP: Awesome flexibility without the complexity",
            ],
            Month.OCTOBER: [
                "Micronaut AOP: Awesome flexibility without the complexity",
            ],
        }

    @Cacheable  # <3>
    def headlines(self, month: Month) -> list[str]:
        sleep(3)  # <4>
        return self._headlines[month]

    @CachePut(parameters=["month"])  # <5>
    def add_headline(self, month: Month, headline: str) -> list[str]:
        headlines = [*self._headlines.get(month, []), headline]
        self._headlines[month] = headlines
        return headlines

    @CacheInvalidate(parameters=["month"])  # <6>
    def remove_headline(self, month: Month, headline: str) -> None:
        if month in self._headlines:
            self._headlines[month] = [
                existing for existing in self._headlines[month] if existing != headline
            ]
