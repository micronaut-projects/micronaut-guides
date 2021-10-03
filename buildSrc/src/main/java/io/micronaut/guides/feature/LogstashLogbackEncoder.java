package io.micronaut.guides.feature;

import javax.inject.Singleton;

@Singleton
public class LogstashLogbackEncoder extends AbstractFeature {

    public LogstashLogbackEncoder() {
        super("logstash-logback-encoder");
    }
}
