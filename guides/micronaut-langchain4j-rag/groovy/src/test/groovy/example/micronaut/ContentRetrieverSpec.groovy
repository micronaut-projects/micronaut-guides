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

import dev.langchain4j.data.document.Document
import dev.langchain4j.rag.content.Content
import dev.langchain4j.rag.content.retriever.ContentRetriever
import dev.langchain4j.rag.query.Query
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import spock.lang.Specification

@MicronautTest(startApplication = false) // <1>
class ContentRetrieverSpec extends Specification {

    @Inject
    ContentRetriever contentRetriever // <2>

    void "retrieves the relevant document"() {
        when:
        List<Content> contents = contentRetriever.retrieve(Query.from("How heavy can my suitcase be?")) // <3>

        then:
        !contents.isEmpty()
        contents.first().textSegment().metadata().getString(Document.FILE_NAME) == "baggage.txt" // <4>
    }
}
