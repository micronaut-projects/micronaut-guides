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

import io.micronaut.core.annotation.Introspected;
import io.micronaut.core.annotation.NonNull;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.Objects;

@Introspected
public class BookInventory {

    @NonNull
    @NotBlank
    private String isbn;

    @NonNull
    @NotNull
    private Integer stock;

    public BookInventory() {}

    public BookInventory(@NonNull @NotBlank String isbn, @NonNull @NotNull Integer stock) {
        this.isbn = isbn;
        this.stock = stock;
    }

    @NonNull
    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(@NonNull String isbn) {
        this.isbn = isbn;
    }

    @NonNull
    public Integer getStock() {
        return stock;
    }

    public void setStock(@NonNull Integer stock) {
        this.stock = stock;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BookInventory that = (BookInventory) o;

        if (!isbn.equals(that.isbn)) return false;
        return stock.equals(that.stock);
    }

    @Override
    public int hashCode() {
        int result = isbn.hashCode();
        result = 31 * result + stock.hashCode();
        return result;
    }
}
