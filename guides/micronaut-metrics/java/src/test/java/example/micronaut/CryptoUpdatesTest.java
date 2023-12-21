/*
 * Copyright 2017-2023 original authors
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
package example.micronaut;

import example.micronaut.crypto.CryptoService;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import io.micronaut.context.ApplicationContext;
import io.micronaut.context.annotation.Requires;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.QueryValue;
import io.micronaut.runtime.server.EmbeddedServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.Collections;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;

@TestInstance(PER_CLASS)
public class CryptoUpdatesTest {

    EmbeddedServer embeddedServer;
    EmbeddedServer kucoinEmbeddedServer;

    @BeforeAll
    void beforeAll() {

        kucoinEmbeddedServer = ApplicationContext.run(EmbeddedServer.class,
                Collections.singletonMap("spec.name", "MetricsTestKucoin"));

        embeddedServer = ApplicationContext.run(EmbeddedServer.class,
                Collections.singletonMap("micronaut.http.services.kucoin.url", "http://localhost:" + kucoinEmbeddedServer.getPort()));
    }

    @AfterAll
    void afterAll() {
        embeddedServer.close();
        kucoinEmbeddedServer.close();
    }

    @Test
    void testCryptoUpdates() {
        CryptoService cryptoService = embeddedServer.getApplicationContext().getBean(CryptoService.class);
        MeterRegistry meterRegistry = embeddedServer.getApplicationContext().getBean(MeterRegistry.class);

        Counter counter = meterRegistry.counter("bitcoin.price.checks");
        Timer timer = meterRegistry.timer("bitcoin.price.time");

        assertEquals(0, counter.count(), 0.000001);
        assertEquals(0, timer.totalTime(MILLISECONDS));

        int checks = 3;

        for (int i = 0; i < checks; i++) {
            cryptoService.updatePrice();
        }

        assertEquals(checks, counter.count(), 0.000001);
        assertTrue(timer.totalTime(MILLISECONDS) > 0);
    }

    @Requires(property = "spec.name", value = "MetricsTestKucoin")
    @Controller
    static class MockKucoinController {
        private static final String RESPONSE = "" +
                "{" +
                "   'code':'200000'," +
                "   'data':{" +
                "      'time':1654865889872," +
                "      'sequence':'1630823934334'," +
                "      'price':'29670.4'," +
                "      'size':'0.00008436'," +
                "      'bestBid':'29666.4'," +
                "      'bestBidSize':'0.16848947'," +
                "      'bestAsk':'29666.5'," +
                "      'bestAskSize':'2.37840044'" +
                "   }" +
                "}";

        @Get("/api/v1/market/orderbook/level1")
        String latest(@QueryValue String symbol) {
            return RESPONSE.replaceAll("'", "\"");
        }
    }
}
