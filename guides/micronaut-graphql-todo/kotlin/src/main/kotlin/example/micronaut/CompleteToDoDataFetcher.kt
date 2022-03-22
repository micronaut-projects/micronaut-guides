package example.micronaut

import graphql.schema.DataFetcher
import graphql.schema.DataFetchingEnvironment
import jakarta.inject.Singleton

@Singleton // <1>
class CompleteToDoDataFetcher(
    private val toDoRepository: ToDoRepository // <2>
) : DataFetcher<Boolean> {

    override fun get(env: DataFetchingEnvironment): Boolean {
        val id = env.getArgument<String>("id").toLong()

        return toDoRepository
            .findById(id) // <3>
            .map { todo -> setCompletedAndUpdate(todo!!) }
            .orElse(false)
    }

    private fun setCompletedAndUpdate(todo: ToDo): Boolean {
        todo.isCompleted = true // <4>
        toDoRepository.update(todo) // <5>
        return true
    }
}