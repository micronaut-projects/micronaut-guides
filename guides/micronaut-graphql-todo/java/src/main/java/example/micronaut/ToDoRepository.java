package example.micronaut;

import jakarta.inject.Singleton;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

@Singleton
public class ToDoRepository {

    private final Map<String, ToDo> toDos = new LinkedHashMap<>();

    public Iterable<ToDo> findAll() {
        return toDos.values();
    }

    public ToDo findById(String id) {
        return toDos.get(id);
    }

    public ToDo save(ToDo toDo) {
        if (toDo.getId() == null) {
            toDo.setId(UUID.randomUUID().toString());
        }
        toDos.put(toDo.getId(), toDo);
        return toDo;
    }
}
