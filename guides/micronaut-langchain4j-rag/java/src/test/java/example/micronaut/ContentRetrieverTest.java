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

import dev.langchain4j.data.document.Document;
import dev.langchain4j.rag.content.Content;
import dev.langchain4j.rag.content.retriever.ContentRetriever;
import dev.langchain4j.rag.query.Query;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@MicronautTest(startApplication = false) // <1>
class ContentRetrieverTest {

    @Test
    void retrievesTheRelevantDocument(ContentRetriever contentRetriever) { // <2>
        List<Content> contents = contentRetriever.retrieve(Query.from("How heavy can my suitcase be?")); // <3>

        assertFalse(contents.isEmpty());
        assertEquals("baggage.txt", contents.getFirst().textSegment().metadata().getString(Document.FILE_NAME)); // <4>
    }
}
