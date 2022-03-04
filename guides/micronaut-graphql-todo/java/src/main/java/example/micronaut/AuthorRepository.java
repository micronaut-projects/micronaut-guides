package example.micronaut;

import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Singleton
public class AuthorRepository {

    private static final Logger LOG = LoggerFactory.getLogger(AuthorRepository.class);

    private final Map<String, Author> authors = new HashMap<>();

    public List<Author> findAllById(Collection<String> ids) {
        LOG.debug("Batch loading authors: {}", ids);

        return authors.values()
                .stream()
                .filter(it -> ids.contains(it.getId()))
                .collect(Collectors.toList());
    }

    public Author findOrCreate(String username) {
        if (!authors.containsKey(username)) {
            authors.put(username, new Author(UUID.randomUUID().toString(), username));
        }

        return authors.get(username);
    }

}
