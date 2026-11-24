/*
 * Copyright 2017-2026 original authors
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

import io.mateu.uidl.interfaces.CrudStore;
import jakarta.inject.Singleton;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Singleton // <1>
public class ProductStore implements CrudStore<Product> { // <2>

    private final Map<String, Product> products = new ConcurrentHashMap<>();

    public ProductStore() {
        save(new Product("P-001", "Espresso machine", 249.0, ProductStatus.Available));
        save(new Product("P-002", "Coffee grinder", 89.5, ProductStatus.Available));
        save(new Product("P-003", "Milk frother", 39.9, ProductStatus.OutOfStock));
        save(new Product("P-004", "Pour-over kettle", 59.0, ProductStatus.Discontinued));
    }

    @Override
    public Optional<Product> findById(String id) {
        return Optional.ofNullable(products.get(id));
    }

    @Override
    public String save(Product product) {
        products.put(product.id(), product);
        return product.id();
    }

    @Override
    public List<Product> findAll() {
        return products.values().stream()
                .sorted(Comparator.comparing(Product::id))
                .toList();
    }

    @Override
    public void deleteAllById(List<String> ids) {
        ids.forEach(products::remove);
    }
}
