package example.micronaut;

import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import jakarta.inject.Singleton;
import org.dataloader.DataLoader;

import java.util.concurrent.CompletionStage;

@Singleton // <1>
public class AuthorDataFetcher implements DataFetcher<CompletionStage<Author>> {

    @Override
    public CompletionStage<Author> get(DataFetchingEnvironment environment) {
        ToDo toDo = environment.getSource();
        DataLoader<Long, Author> authorDataLoader = environment.getDataLoader("author"); // <2>
        return authorDataLoader.load(toDo.getAuthorId());
    }
}