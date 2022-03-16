package example.micronaut;

import io.micronaut.data.annotation.GeneratedValue;
import io.micronaut.data.annotation.Id;
import io.micronaut.data.annotation.MappedEntity;

import javax.validation.constraints.NotNull;

import static io.micronaut.data.annotation.GeneratedValue.Type.AUTO;

@MappedEntity // <1>
public class ToDo {

    @Id // <2>
    @GeneratedValue(AUTO)
    private Long id;

    @NotNull
    private String title;

    private boolean completed;

    private final long authorId;

    public ToDo(String title, long authorId) {
        this.title = title;
        this.authorId = authorId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public long getAuthorId() {
        return authorId;
    }
}
