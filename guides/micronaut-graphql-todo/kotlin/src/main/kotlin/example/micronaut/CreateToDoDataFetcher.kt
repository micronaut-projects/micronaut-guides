package example.micronaut

import graphql.schema.DataFetcher
import graphql.schema.DataFetchingEnvironment
import jakarta.inject.Singleton
import jakarta.transaction.Transactional

@Singleton // <1>
open class CreateToDoDataFetcher(
    private val toDoRepository: ToDoRepository,  // <2>
    private val authorRepository: AuthorRepository
) : DataFetcher<ToDo> {

    @Transactional
    override fun get(env: DataFetchingEnvironment): ToDo {
        val title = env.getArgument<String>("title")
        val username = env.getArgument<String>("author")
        val author = authorRepository.findOrCreate(username) // <3>
        val toDo = ToDo(title, author.id!!)
        return toDoRepository.save(toDo) // <4>
    }
}