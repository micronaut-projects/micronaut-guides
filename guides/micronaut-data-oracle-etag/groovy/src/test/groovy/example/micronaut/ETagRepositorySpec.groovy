/*
 * Copyright 2017-2026 original authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 */
package example.micronaut

import io.micronaut.data.exceptions.OptimisticLockException
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import spock.lang.Specification

@MicronautTest(transactional = false)
class ETagRepositorySpec extends Specification {

    @Inject
    ETagBookRepository bookRepository

    @Inject
    ExplicitETagBookRepository explicitBookRepository

    void 'stale etag prevents an update'() {
        when:
        def saved = bookRepository.save(new ETagBook(null, 'Initial', new ETagBook.BookDetails(200, 10), null))
        def fresh = bookRepository.findById(saved.id).orElseThrow() // <1>

        then:
        fresh.etag != null

        when:
        bookRepository.update(new ETagBook(fresh.id, 'Updated', fresh.details, fresh.etag)) // <2>
        bookRepository.update(new ETagBook(fresh.id, 'Stale', fresh.details, fresh.etag)) // <3>

        then:
        thrown(OptimisticLockException)
    }

    void 'changing an excluded field does not change the etag'() {
        given:
        def saved = explicitBookRepository.save(new ExplicitETagBook(null, 'Oracle', 'Initial notes', null))
        def fresh = explicitBookRepository.findById(saved.id).orElseThrow()

        when:
        explicitBookRepository.update(new ExplicitETagBook(fresh.id, fresh.title, 'Updated notes', fresh.etag))
        def reloaded = explicitBookRepository.findById(fresh.id).orElseThrow()

        then:
        reloaded.notes == 'Updated notes'
        reloaded.etag == fresh.etag
    }
}
