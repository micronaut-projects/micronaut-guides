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

import io.micronaut.configuration.kafka.annotation.KafkaListener;
import io.micronaut.configuration.kafka.annotation.Topic;
import jakarta.inject.Singleton;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static io.micronaut.configuration.kafka.annotation.OffsetReset.EARLIEST;

@Singleton // <1>
@Transactional // <2>
class ProductPriceChangedEventHandler {

    private static final Logger LOG = LoggerFactory.getLogger(ProductPriceChangedEventHandler.class);

    private final ProductRepository productRepository;

    ProductPriceChangedEventHandler(ProductRepository productRepository) { // <3>
        this.productRepository = productRepository;
    }

    @Topic("product-price-changes") // <4>
    @KafkaListener(offsetReset = EARLIEST, groupId = "demo") // <5>
    public void handle(ProductPriceChangedEvent event) {
        LOG.info("Received a ProductPriceChangedEvent with productCode:{}: ", event.productCode());
        productRepository.updateProductPrice(event.productCode(), event.price());
    }
}
