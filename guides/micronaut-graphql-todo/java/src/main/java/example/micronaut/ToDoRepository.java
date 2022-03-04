package example.micronaut;

import jakarta.inject.Singleton;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

@Singleton
public class ToDoRepository {

    private final Map<String, ToDo> toDos = new LinkedHashMap<>();

    public ToDoRepository(AuthorRepository authorRepository) {
        save(new ToDo("Book flights to Gran Canaria", authorRepository.findOrCreate("William").getId()));
        save(new ToDo("Order torrefacto coffee beans", authorRepository.findOrCreate("George").getId()));
        save(new ToDo("Watch La Casa de Papel", authorRepository.findOrCreate("George").getId()));
    }

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

    public void deleteById(String id) {
        toDos.remove(id);
    }
}
