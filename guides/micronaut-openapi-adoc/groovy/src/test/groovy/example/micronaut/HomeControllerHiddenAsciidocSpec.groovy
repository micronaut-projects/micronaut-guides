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

import io.micronaut.core.io.ResourceLoader
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import spock.lang.Specification

import java.nio.charset.StandardCharsets

@MicronautTest(startApplication = false) // <1>
class HomeControllerHiddenAsciidocSpec extends Specification {

    @Inject
    ResourceLoader resourceLoader

    void 'home controller is hidden'() {
        given:
        Optional<InputStream> adocInputStream = resourceLoader.getResourceAsStream('META-INF/swagger/micronaut-guides-1.0.adoc')

        expect:
        adocInputStream.present

        when:
        String adoc = new String(adocInputStream.get().readAllBytes(), StandardCharsets.UTF_8)

        then:
        !adoc.contains('=== __GET__ `/`')
    }
}
