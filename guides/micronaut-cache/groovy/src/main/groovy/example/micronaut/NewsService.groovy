package example.micronaut

import groovy.transform.CompileStatic
import io.micronaut.cache.annotation.CacheConfig
import io.micronaut.cache.annotation.CacheInvalidate
import io.micronaut.cache.annotation.CachePut
import io.micronaut.cache.annotation.Cacheable
import javax.inject.Singleton
import java.time.Month
import java.util.concurrent.TimeUnit

@CompileStatic
@Singleton // <1>
@CacheConfig("headlines") // <2>
class NewsService {

    Map<Month, List<String>> headlines = [
        (Month.NOVEMBER): ["Micronaut Graduates to Trial Level in Thoughtworks technology radar Vol.1",
                "Micronaut AOP: Awesome flexibility without the complexity"],
        (Month.OCTOBER): ["Micronaut AOP: Awesome flexibility without the complexity"]
    ]

    @Cacheable // <3>
    List<String> headlines(Month month) {
        TimeUnit.SECONDS.sleep(3) // <4>
        headlines[month]
    }

    @CachePut(parameters = ["month"]) // <5>
    List<String> addHeadline(Month month, String headline) {
        if (headlines.containsKey(month)) {
            List<String> lines = headlines[month]
            lines << headline
            headlines.put(month, lines)
        } else {
            headlines[month] = [headline]
        }
        headlines[month]
    }

    @CacheInvalidate(parameters = ["month"]) // <6>
    void removeHeadline(Month month, String headline) {
        if (headlines.containsKey(month)) {
            List<String> lines = headlines[month]
            lines -= headline
            headlines.put(month, lines)
        }
    }
}
