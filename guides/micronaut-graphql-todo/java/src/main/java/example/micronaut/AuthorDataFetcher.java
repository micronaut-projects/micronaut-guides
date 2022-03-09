package example.micronaut;

import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import jakarta.inject.Singleton;
import org.dataloader.DataLoader;

import java.util.concurrent.CompletionStage;

@Singleton
public class AuthorDataFetcher implements DataFetcher<CompletionStage<Author>> {

    @Override
    public CompletionStage<Author> get(DataFetchingEnvironment environment) throws Exception {
        ToDo toDo = environment.getSource();
        DataLoader<Long, Author> authorDataLoader = environment.getDataLoader("author");
        return authorDataLoader.load(toDo.getAuthorId());
    }
}