package example.micronaut.entities;

import io.micronaut.core.annotation.Nullable;
import io.micronaut.data.annotation.GeneratedValue;
import io.micronaut.data.annotation.Id;
import io.micronaut.data.annotation.MappedEntity;
import io.micronaut.data.annotation.Relation;

import jakarta.validation.constraints.NotNull;
import java.util.List;

@MappedEntity // <1>
public class Room {
    @Id // <2>
    @GeneratedValue(GeneratedValue.Type.AUTO) // <3>
    private Long id;

    @NotNull
    private String name;

    @Relation(value = Relation.Kind.ONE_TO_MANY, mappedBy = "room") // <4>
    @Nullable
    private List<Message> messages;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Nullable
    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(@Nullable List<Message> messages) {
        this.messages = messages;
    }

    @Override
    public String toString() {
        return "Room{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
