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

import io.micronaut.data.exceptions.OptimisticLockException;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@MicronautTest(transactional = false)
class OptimisticLockingTest {

    @Inject
    BookRepository bookRepository;

    @Inject
    ArticleRepository articleRepository;

    @Test
    void staleEtagPreventsAnUpdate() {
        Book saved = bookRepository.save(new Book(null, "Initial", new Book.BookDetails(200, 10), null));
        Book fresh = bookRepository.findById(saved.id()).orElseThrow(); // <1>
        assertNotNull(fresh.etag());

        bookRepository.update(new Book(fresh.id(), "Updated", fresh.details(), fresh.etag())); // <2>

        Book stale = new Book(fresh.id(), "Stale", fresh.details(), fresh.etag()); // <3>
        assertThrows(OptimisticLockException.class, () -> bookRepository.update(stale));
    }

    @Test
    void changingAnExcludedFieldDoesNotChangeTheEtag() {
        Article saved = articleRepository.save(
            new Article(null, "Oracle", "Initial notes", null));
        Article fresh = articleRepository.findById(saved.id()).orElseThrow();

        articleRepository.update(new Article(
            fresh.id(), fresh.title(), "Updated notes", fresh.etag()));

        Article reloaded = articleRepository.findById(fresh.id()).orElseThrow();
        assertEquals("Updated notes", reloaded.notes());
        assertEquals(fresh.etag(), reloaded.etag());
    }
}
