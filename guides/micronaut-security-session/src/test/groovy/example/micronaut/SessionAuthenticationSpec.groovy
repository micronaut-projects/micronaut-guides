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
import io.micronaut.runtime.server.EmbeddedServer
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject

@MicronautTest // <1>
class SessionAuthenticationSpec extends GebSpec {

    @Inject
    EmbeddedServer embeddedServer // <2>

    def "verify session based authentication works"() {
        given:
        browser.baseUrl = "http://localhost:${embeddedServer.port}"

        when:
        to HomePage

        then:
        at HomePage

        when:
        HomePage homePage = browser.page HomePage

        then: 'As we are not logged in, there is no username'
        homePage.username() == null

        when: 'click the login link'
        homePage.login()

        then:
        at LoginPage

        when: 'fill the login form, with invalid credentials'
        LoginPage loginPage = browser.page LoginPage
        loginPage.login('foo', 'foo')

        then: 'the user is still in the login form'
        at LoginPage

        and: 'and error is displayed'
        loginPage.hasErrors()

        when: 'fill the form with wrong credentials'
        loginPage.login('sherlock', 'foo')

        then: 'we get redirected to the home page'
        at LoginFailedPage

        when: 'fill the form with valid credentials'
        loginPage.login('sherlock', 'password')

        then: 'we get redirected to the home page'
        at HomePage

        when:
        homePage = browser.page HomePage

        then: 'the username is populated'
        homePage.username() == 'sherlock'

        when: 'click the logout button'
        homePage.logout()

        then: 'we are in the home page'
        at HomePage

        when:
        homePage = browser.page HomePage

        then: 'but we are no longer logged in'
        homePage.username() == null
    }
}
