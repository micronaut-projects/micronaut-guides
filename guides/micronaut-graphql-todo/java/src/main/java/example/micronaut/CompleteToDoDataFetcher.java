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
        Long id = Long.valueOf(env.getArgument("id"));

        return toDoRepository.findById(id).map(todo -> { // <1>
                    todo.setCompleted(true); // <2>
                    toDoRepository.update(todo); // <3>
                    return true;
                })
                .orElse(false);
    }
}
