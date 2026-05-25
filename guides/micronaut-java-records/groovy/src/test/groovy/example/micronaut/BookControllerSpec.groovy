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
package example.micronaut
import io.micronaut.context.annotation.Property
import io.micronaut.core.type.Argument
import io.micronaut.http.HttpRequest
import io.micronaut.http.client.BlockingHttpClient
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import spock.lang.Specification

@Property(name = 'vat.percentage', value = '21.0') // <1>
@MicronautTest(transactional = false) // <2>
class BookControllerSpec extends Specification {

    int booksInsertedInDbByMigration = 1

    @Inject
    @Client('/')
    HttpClient httpClient // <3>

    @Inject
    BookRepository bookRepository

    void 'records used for JSON serialization'() {
        given:
        String title = 'Building Microservices'
        String isbn = '1491950358'
        // <4>
        String about = '''
                        Distributed systems have become more fine-grained in the past 10 years, shifting from code-heavy monolithic applications to smaller, self-contained microservices. But developing these systems brings its own set of headaches. With lots of examples and practical advice, this book takes a holistic view of the topics that system architects and administrators must consider when building, managing, and evolving microservice architectures.

                        Microservice technologies are moving quickly. Author Sam Newman provides you with a firm grounding in the concepts while diving into current solutions for modeling, integrating, testing, deploying, and monitoring your own autonomous services. You'll follow a fictional company throughout the book to learn how building a microservice architecture affects a single domain.

                        Discover how microservices allow you to align your system design with your organization's goals
                        Learn options for integrating a service with the rest of your system
                        Take an incremental approach when splitting monolithic codebases
                        Deploy individual microservices through continuous integration
                        Examine the complexities of testing and monitoring distributed services
                        Manage security with user-to-service and service-to-service models
                        Understand the challenges of scaling microservice architectures
                        '''
        Book b = new Book(isbn,
                title,
                new BigDecimal('38.15'),
                about)

        when:
        Book book = bookRepository.save(b)

        then:
        bookRepository.count() == booksInsertedInDbByMigration + 1

        when:
        BlockingHttpClient client = httpClient.toBlocking()
        List<BookForSale> books = client.retrieve(HttpRequest.GET('/books'),
                Argument.listOf(BookForSale)) // <5>

        then:
        books
        books.size() == booksInsertedInDbByMigration + 1
        books[1].title() == 'Building Microservices'
        books[1].isbn() == '1491950358'
        books[1].price() == new BigDecimal('46.16')

        cleanup:
        if (book) {
            bookRepository.delete(book)
        }
    }
}
