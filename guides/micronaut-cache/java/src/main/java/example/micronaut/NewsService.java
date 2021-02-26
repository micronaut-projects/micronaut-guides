package example.micronaut;

import io.micronaut.cache.annotation.CacheConfig;
import io.micronaut.cache.annotation.CacheInvalidate;
import io.micronaut.cache.annotation.CachePut;
import io.micronaut.cache.annotation.Cacheable;

import javax.inject.Singleton;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Singleton // <1>
@CacheConfig("headlines") // <2>
public class NewsService {

    Map<Month, List<String>> headlines = new HashMap<Month, List<String>>() {{
        put(Month.NOVEMBER, Arrays.asList("Micronaut Graduates to Trial Level in Thoughtworks technology radar Vol.1",
                "Micronaut AOP: Awesome flexibility without the complexity"));
        put(Month.OCTOBER, Collections.singletonList("Micronaut AOP: Awesome flexibility without the complexity"));
    }};

    @Cacheable // <3>
    public List<String> headlines(Month month) {
        try {
            TimeUnit.SECONDS.sleep(3); // <4>
            return headlines.get(month);
        } catch (InterruptedException e) {
            return null;
        }
    }

    @CachePut(parameters = {"month"}) // <5>
    public List<String> addHeadline(Month month, String headline) {
        if (headlines.containsKey(month)) {
            List<String> l = new ArrayList<>(headlines.get(month));
            l.add(headline);
            headlines.put(month, l);
        } else {
            headlines.put(month, Arrays.asList(headline));
        }
        return headlines.get(month);
    }

    @CacheInvalidate(parameters = {"month"}) // <6>
    public void removeHeadline(Month month, String headline) {
        if (headlines.containsKey(month)) {
            List<String> l = new ArrayList<>(headlines.get(month));
            l.remove(headline);
            headlines.put(month, l);
        }
    }
}
