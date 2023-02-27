/*
 * Copyright 2017-2023 original authors original authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.micronaut.guides;

import io.micronaut.configuration.picocli.PicocliRunner;
import io.micronaut.guides.httpclient.Guide;
import io.micronaut.guides.httpclient.GuidesClient;
import jakarta.inject.Inject;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import reactor.core.publisher.Mono;
import java.util.ArrayList;
import java.util.List;

@Command(name = "guidetweets", description = "generates a list of tweets", mixinStandardHelpOptions = true)
public class TweetsCommand implements Runnable {
    private static final String HASHTAG = "#";

    @Inject
    GuidesClient guidesClient;

    @Option(names = {"-#", "--hashtag"}, description = "Twitter Hashtag. E.g. micronaut", defaultValue = "micronaut")
    String hashtag;

    @Option(names = {"-p", "--prefix"}, description = "Tweet prefix", defaultValue =  "📖 Micronaut Guide:")
    String prefix;

    public static void main(String[] args) {
        PicocliRunner.run(TweetsCommand.class, args);
    }

    public void run() {
        List<Guide> guides = Mono.from(guidesClient.fetchGuides()).block();
        composeTweets(guides).forEach(System.out::println);
    }

    private List<String> composeTweets(List<Guide> guides) {
        List<String> tweets = new ArrayList<>();
        for (Guide guide : guides) {
            String tweet = prefix.endsWith(" ") ? prefix : (prefix + " ") +
                    guide.getTitle() +
                    " " + guide.getUrl() +  " " +
                    (hashtag.startsWith(HASHTAG) ? hashtag : HASHTAG + hashtag);
            tweets.add(tweet);
        }
        return tweets;
    }
}
