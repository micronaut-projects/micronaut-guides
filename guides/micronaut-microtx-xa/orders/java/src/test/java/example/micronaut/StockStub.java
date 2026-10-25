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

import io.micronaut.context.annotation.Requires;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.QueryValue;
import io.micronaut.http.exceptions.HttpStatusException;

/**
 * A stock service that runs in the test and has no database.
 */
@Requires(property = "stock.stub", value = "true")
@Controller("/stock")
public class StockStub {

    @Post("/{item}/reserve")
    public void reserve(String item, @QueryValue int quantity) {
        if (quantity > 10) {
            throw new HttpStatusException(HttpStatus.CONFLICT, "Not enough " + item + " in stock");
        }
    }
}
