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

import dev.langchain4j.data.segment.TextSegment
import dev.langchain4j.model.embedding.EmbeddingModel
import dev.langchain4j.model.embedding.onnx.allminilml6v2.AllMiniLmL6V2EmbeddingModel
import dev.langchain4j.rag.content.retriever.ContentRetriever
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever
import dev.langchain4j.store.embedding.EmbeddingStore
import io.micronaut.context.annotation.Factory
import io.micronaut.context.annotation.Primary
import jakarta.inject.Singleton

@Factory // <1>
class RagFactory {

    @Primary // <2>
    @Singleton
    fun embeddingModel(): EmbeddingModel = AllMiniLmL6V2EmbeddingModel() // <3>

    @Singleton
    fun contentRetriever(embeddingStore: EmbeddingStore<TextSegment>, // <4>
                         embeddingModel: EmbeddingModel): ContentRetriever =
        EmbeddingStoreContentRetriever.builder() // <5>
            .embeddingStore(embeddingStore)
            .embeddingModel(embeddingModel)
            .maxResults(2)
            .build()
}
