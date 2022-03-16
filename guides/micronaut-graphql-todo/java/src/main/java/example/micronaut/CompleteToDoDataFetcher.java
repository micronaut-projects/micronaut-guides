package example.micronaut;

import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import jakarta.inject.Singleton;

@Singleton // <1>
public class CompleteToDoDataFetcher implements DataFetcher<Boolean> {

    private final ToDoRepository toDoRepository;

    public CompleteToDoDataFetcher(ToDoRepository toDoRepository) { // <2>
        this.toDoRepository = toDoRepository;
    }

    @Override
    public Boolean get(DataFetchingEnvironment env) {
        long id = Long.parseLong(env.getArgument("id"));
        return toDoRepository
                .findById(id) // <3>
                .map(this::setCompletedAndUpdate)
                .orElse(false);
    }

    private boolean setCompletedAndUpdate(ToDo todo) {
        todo.setCompleted(true); // <4>
        toDoRepository.update(todo); // <5>
        return true;
    }
}
