package example.micronaut;

import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import jakarta.inject.Singleton;

@Singleton
public class DeleteToDoDataFetcher implements DataFetcher<Boolean> {

    private final ToDoRepository toDoRepository;

    public DeleteToDoDataFetcher(ToDoRepository toDoRepository) {
        this.toDoRepository = toDoRepository;
    }

    @Override
    public Boolean get(DataFetchingEnvironment env) {
        String id = env.getArgument("id");
        ToDo toDo = toDoRepository.findById(id);
        if (toDo != null) {
            toDoRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }
}
