package com.example.boardgamebuddy;

import com.github.tomakehurst.wiremock.WireMockServer;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.test.support.TestPropertyProvider;

import java.util.Map;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

public abstract class WireMockTestPropertyProvider implements TestPropertyProvider {

    private WireMockServer wireMockServer;

    protected WireMockServer getWireMockServer() {
        if (wireMockServer == null) {
            wireMockServer = new WireMockServer(wireMockConfig().dynamicPort());
            wireMockServer.start();
        }
        return wireMockServer;
    }

    void stopWireMock() {
        if (wireMockServer != null && wireMockServer.isRunning()) {
            wireMockServer.stop();
        }
    }
}
