package example.micronaut;

import io.micronaut.data.model.vector.FloatVector;
import io.micronaut.data.model.vector.search.Score;
import io.micronaut.data.model.vector.search.ScoringFunction;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.QueryValue;
import io.micronaut.http.exceptions.HttpStatusException;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.annotation.ExecuteOn;
import io.micronaut.serde.annotation.Serdeable;

import java.util.List;

@ExecuteOn(TaskExecutors.BLOCKING)
@Controller("/documents")
final class DocumentController {

    private final DocumentRepository documents;
    private final Embeddings embeddings;

    DocumentController(DocumentRepository documents, Embeddings embeddings) {
        this.documents = documents;
        this.embeddings = embeddings;
    }

    @Post
    HttpResponse<DocumentCreated> create(@Body DocumentRequest request) {
        var content = required(request.content()); // <1>
        var saved = documents.save(new Document(null, content, embeddings.embed(content)));
        return HttpResponse.created(new DocumentCreated(saved.id(), saved.content()));
    }

    @Get("/search")
    List<Match> search(@QueryValue String q) {
        return documents.searchTop3ByEmbeddingNear(
                        embeddings.embed(required(q)), // <2>
                        new Score(2),
                        ScoringFunction.COSINE // <3>
                ).results().stream()
                .map(result -> new Match(
                        result.entity().id(),
                        result.entity().content(),
                        result.similarity().value()
                ))
                .toList();
    }

    private static String required(String text) {
        if (text == null || text.isBlank()) {
            throw new HttpStatusException(HttpStatus.BAD_REQUEST, "text is required");
        }
        return text;
    }

    @Serdeable
    record DocumentRequest(String content) {
    }

    @Serdeable
    record DocumentCreated(Long id, String content) {
    }

    @Serdeable
    record Match(Long id, String content, double similarity) {
    }
}
