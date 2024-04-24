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

import io.micronaut.serde.annotation.Serdeable;

import java.util.Objects;

@Serdeable
public class BookInventory {

    private final String isbn;
    private final Integer stock;

    public BookInventory(String isbn, Integer stock) {
        this.isbn = isbn;
        this.stock = stock;
    }

    public String getIsbn() {
        return isbn;
    }

    public Integer getStock() {
        return stock;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BookInventory that = (BookInventory) o;
        return Objects.equals(isbn, that.isbn) &&
                Objects.equals(stock, that.stock);
    }

    @Override
    public int hashCode() {
        return Objects.hash(isbn, stock);
    }
}
