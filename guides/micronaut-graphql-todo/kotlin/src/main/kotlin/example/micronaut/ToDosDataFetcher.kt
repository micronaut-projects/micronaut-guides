package example.micronaut

import graphql.schema.DataFetcher
import graphql.schema.DataFetchingEnvironment
import jakarta.inject.Singleton

@Singleton // <1>
class ToDosDataFetcher(
    private val toDoRepository: ToDoRepository // <2>
) : DataFetcher<Iterable<ToDo?>> {

    override fun get(env: DataFetchingEnvironment): Iterable<ToDo?> {
        return toDoRepository.findAll()
    }
}