package example.micronaut;

import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import jakarta.inject.Singleton;

@Singleton // <1>
public class ToDosDataFetcher implements DataFetcher<Iterable<ToDo>> {

    private final ToDoRepository toDoRepository;

    public ToDosDataFetcher(ToDoRepository toDoRepository) { // <2>
        this.toDoRepository = toDoRepository;
    }

    @Override
    public Iterable<ToDo> get(DataFetchingEnvironment env) {
        return toDoRepository.findAll();
    }
}
