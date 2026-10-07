package example.micronaut

import groovy.transform.Canonical
import groovy.transform.CompileStatic
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

@CompileStatic
@ExecuteOn(TaskExecutors.BLOCKING)
@Controller('/documents')
class DocumentController {

    private final DocumentRepository documents
    private final Embeddings embeddings

    DocumentController(DocumentRepository documents, Embeddings embeddings) {
        this.documents = documents
        this.embeddings = embeddings
    }

    @Post
    HttpResponse<DocumentCreated> create(@Body DocumentRequest request) {
        String content = required(request.content) // <1>
        Document saved = documents.save(new Document(content: content, embedding: embeddings.embed(content)))
        HttpResponse.created(new DocumentCreated(saved.id, saved.content))
    }

    @Get('/search')
    List<Match> search(@QueryValue String q) {
        documents.searchTop3ByEmbeddingNear(
                embeddings.embed(required(q)), // <2>
                new Score(2),
                ScoringFunction.COSINE // <3>
        ).results().collect { result ->
            new Match(result.entity().id, result.entity().content, result.similarity().value())
        }
    }

    private static String required(String text) {
        if (text == null || text.isBlank()) {
            throw new HttpStatusException(HttpStatus.BAD_REQUEST, 'text is required')
        }
        text
    }

    @Serdeable
    @Canonical
    static class DocumentRequest {
        String content
    }

    @Serdeable
    @Canonical
    static class DocumentCreated {
        Long id
        String content
    }

    @Serdeable
    @Canonical
    static class Match {
        Long id
        String content
        double similarity
    }
}
