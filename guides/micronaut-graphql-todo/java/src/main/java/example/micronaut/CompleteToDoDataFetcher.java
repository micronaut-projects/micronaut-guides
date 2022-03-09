package example.micronaut;

import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import jakarta.inject.Singleton;

import java.util.Optional;

@Singleton
public class CompleteToDoDataFetcher implements DataFetcher<Boolean> {

    private final ToDoRepository toDoRepository;

    public CompleteToDoDataFetcher(ToDoRepository toDoRepository) {
        this.toDoRepository = toDoRepository;
    }

    @Override
    public Boolean get(DataFetchingEnvironment env) {
        Long id = Long.valueOf(env.getArgument("id"));
        Optional<ToDo> maybeToDo = toDoRepository.findById(id);
        if (maybeToDo.isPresent()) {
            ToDo toDo = maybeToDo.get();
            toDo.setCompleted(true);
            toDoRepository.update(toDo);
            return true;
        } else {
            return false;
        }
    }
}
