package io.micronaut.guides.feature;

import jakarta.inject.Singleton;

@Singleton
public class WcChatbot extends AbstractFeature {

    public WcChatbot() {
        super("wc-chatbot");
    }
}
