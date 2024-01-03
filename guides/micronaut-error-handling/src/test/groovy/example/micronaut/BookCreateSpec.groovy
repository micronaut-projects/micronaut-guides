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
package example.micronaut

import geb.spock.GebSpec
import io.micronaut.context.ApplicationContext
import io.micronaut.runtime.server.EmbeddedServer
import spock.lang.AutoCleanup
import spock.lang.Shared

class BookCreateSpec extends GebSpec {

    @Shared
    @AutoCleanup
    EmbeddedServer embeddedServer = ApplicationContext.run(EmbeddedServer)

    void "verify validation in the form works"() {
        given:

        browser.baseUrl = "http://localhost:${embeddedServer.port}"

        when:
        go "/books/create"

        then:
        at BookCreatePage

        when:
        BookCreatePage page = browser.page(BookCreatePage)

        then:
        !page.hasErrors()

        when:
        page.save("", 0)

        then:
        at BookCreatePage

        when:
        page = browser.page(BookCreatePage)

        then:
        page.title() == ""
        page.pages() == "0"
        page.hasErrors()
        page.errors().contains('title must not be blank')
        page.errors().contains('pages must be greater than 0')

    }
}
