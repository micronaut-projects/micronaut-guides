package example.micronaut

import io.micronaut.data.model.vector.search.Score
import io.micronaut.data.model.vector.search.ScoringFunction
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Post
import io.micronaut.http.annotation.QueryValue
import io.micronaut.http.exceptions.HttpStatusException
import io.micronaut.scheduling.TaskExecutors
import io.micronaut.scheduling.annotation.ExecuteOn
import io.micronaut.serde.annotation.Serdeable

@ExecuteOn(TaskExecutors.BLOCKING)
@Controller("/documents")
class DocumentController(private val documents: DocumentRepository, private val embeddings: Embeddings) {

    @Post
    fun create(@Body request: DocumentRequest): HttpResponse<DocumentCreated> {
        val content = required(request.content) // <1>
        val saved = documents.save(Document(null, content, embeddings.embed(content)))
        return HttpResponse.created(DocumentCreated(saved.id, saved.content))
    }

    @Get("/search")
    fun search(@QueryValue q: String): List<Match> =
        documents.searchTop3ByEmbeddingNear(
            embeddings.embed(required(q)), // <2>
            Score(2.0),
            ScoringFunction.COSINE // <3>
        ).results().map { result ->
            Match(result.entity().id, result.entity().content, checkNotNull(result.similarity()).value())
        }

    private fun required(text: String?): String {
        if (text.isNullOrBlank()) {
            throw HttpStatusException(HttpStatus.BAD_REQUEST, "text is required")
        }
        return text
    }

    @Serdeable
    data class DocumentRequest(val content: String? = null)

    @Serdeable
    data class DocumentCreated(val id: Long?, val content: String)

    @Serdeable
    data class Match(val id: Long?, val content: String, val similarity: Double)
}
