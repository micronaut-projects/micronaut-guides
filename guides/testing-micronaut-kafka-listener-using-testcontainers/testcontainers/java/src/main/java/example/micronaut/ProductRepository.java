/*
 * Copyright 2017-2024 original authors
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

import io.micronaut.data.annotation.Query;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.util.Optional;

@Repository // <1>
public interface ProductRepository extends JpaRepository<Product, Long> { // <2>

    Optional<Product> findByCode(String code);

    @Query("update Product p set p.price = :price where p.code = :productCode") // <3>
    void updateProductPrice(String productCode, BigDecimal price);
}
