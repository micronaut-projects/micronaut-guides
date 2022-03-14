package example.micronaut;

import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import jakarta.inject.Singleton;

@Singleton
public class CompleteToDoDataFetcher implements DataFetcher<Boolean> {

    private final ToDoRepository toDoRepository;

    public CompleteToDoDataFetcher(ToDoRepository toDoRepository) {
        this.toDoRepository = toDoRepository;
    }

    @Override
    public Boolean get(DataFetchingEnvironment env) {
        long id = Long.parseLong(env.getArgument("id"));

        return toDoRepository
                .findById(id)
                .map(this::setCompletedAndUpdate)
                .orElse(false);
    }

    private boolean setCompletedAndUpdate(ToDo todo) {
        todo.setCompleted(true);
        toDoRepository.update(todo);
        return true;
    }
}
