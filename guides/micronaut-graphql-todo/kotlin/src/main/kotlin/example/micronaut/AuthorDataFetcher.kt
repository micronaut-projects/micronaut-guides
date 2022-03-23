package example.micronaut

import graphql.schema.DataFetcher
import graphql.schema.DataFetchingEnvironment
import jakarta.inject.Singleton
import java.util.concurrent.CompletionStage

@Singleton // <1>
class AuthorDataFetcher : DataFetcher<CompletionStage<Author>> {

    override fun get(environment: DataFetchingEnvironment): CompletionStage<Author> {
        val toDo: ToDo = environment.getSource()
        val authorDataLoader = environment.getDataLoader<Long, Author>("author") // <2>
        return authorDataLoader.load(toDo.authorId)
    }
}