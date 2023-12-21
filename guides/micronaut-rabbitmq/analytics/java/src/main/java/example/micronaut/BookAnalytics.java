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

import io.micronaut.core.annotation.Creator;
import io.micronaut.serde.annotation.Serdeable;

@Serdeable
public class BookAnalytics {

    private final String bookIsbn;
    private final long count;

    @Creator
    public BookAnalytics(String bookIsbn, long count) {
        this.bookIsbn = bookIsbn;
        this.count = count;
    }

    public String getBookIsbn() {
        return bookIsbn;
    }

    public long getCount() {
        return count;
    }
}
