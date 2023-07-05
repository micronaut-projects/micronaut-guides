package example.micronaut;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.util.CollectionUtils;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.*;

@Singleton
public class DefaultBookRepository extends DynamoRepository<Book> implements BookRepository {
    private static final Logger LOG = LoggerFactory.getLogger(DefaultBookRepository.class);
    private static final String ATTRIBUTE_ID = "id";
    private static final String ATTRIBUTE_ISBN = "isbn";
    private static final String ATTRIBUTE_NAME = "name";
    private static final String ATTRIBUTE_STOCK = "stock";
    public static final String PREFIX_ISBN = "ISBN";

    public DefaultBookRepository(DynamoDbClient dynamoDbClient,
                                 DynamoConfiguration dynamoConfiguration) {
        super(dynamoDbClient, dynamoConfiguration);
    }

    @Override
    public void save(@NonNull @NotNull @Valid Book book) {
        PutItemResponse itemResponse = dynamoDbClient.putItem(PutItemRequest.builder()
                .tableName(dynamoConfiguration.getTableName())
                .item(item(book))
                .build());
        if (LOG.isDebugEnabled()) {
            LOG.debug(itemResponse.toString());
        }
    }

    @Override
    @NonNull
    public Optional<Book> findById(@NonNull @NotBlank String id) {
        return findById(Book.class, id)
                .map(this::bookOf);
    }

    @Override
    public void delete(@NonNull @NotBlank String id) {
        delete(Book.class, id);
    }

    @Override
    @NonNull
    public Optional<Book> findByIsbn(@NonNull @NotBlank String isbn) {
        QueryRequest request = QueryRequest.builder()
                .tableName(dynamoConfiguration.getTableName())
                .indexName(INDEX_GSI_2)
                .limit(1)
                .keyConditionExpression("#pk = :pk")
                .expressionAttributeNames(Collections.singletonMap("#pk", ATTRIBUTE_GSI_2_PK))
                .expressionAttributeValues(Collections.singletonMap(":pk",
                        AttributeValue.builder().s(PREFIX_ISBN + HASH + isbn).build()))
                .build();
        QueryResponse response = dynamoDbClient.query(request);
        if (LOG.isTraceEnabled()) {
            LOG.trace(response.toString());
        }
        List<Book> result = parseInResponse(response);
        return CollectionUtils.isEmpty(result) ?
                Optional.empty() :
                Optional.of(result.get(0));
    }

    @Override
    @NonNull
    public List<Book> findAll() {
        List<Book> result = new ArrayList<>();
        String beforeId = null;
        do {
            QueryRequest request = findAllQueryRequest(Book.class, beforeId, null);
            QueryResponse response = dynamoDbClient.query(request);
            if (LOG.isTraceEnabled()) {
                LOG.trace(response.toString());
            }
            result.addAll(parseInResponse(response));
            beforeId = lastEvaluatedId(response, Book.class).orElse(null);
            LOG.trace("Before ID {}", beforeId);
        } while(beforeId != null);
        return result;
    }

    private List<Book> parseInResponse(QueryResponse response) {
        List<Map<String, AttributeValue>> items = response.items();
        List<Book> result = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(items)) {
            for (Map<String, AttributeValue> item : items) {
                result.add(bookOf(item));
            }
        }
        return result;
    }

    @NonNull
    private Book bookOf(@NonNull Map<String, AttributeValue> item) {
        return new Book(item.get(ATTRIBUTE_ID).s(),
                item.get(ATTRIBUTE_ISBN).s(),
                item.get(ATTRIBUTE_NAME).s(),
                item.containsKey(ATTRIBUTE_STOCK) ? Integer.valueOf(item.get(ATTRIBUTE_STOCK).n()) : null);
    }

    @Override
    @NonNull
    protected Map<String, AttributeValue> item(@NonNull Book book) {
        Map<String, AttributeValue> result = super.item(book);
        result.put(ATTRIBUTE_ID, AttributeValue.builder().s(book.getId()).build());
        result.put(ATTRIBUTE_ISBN, AttributeValue.builder().s(book.isbn()).build());
        result.put(ATTRIBUTE_NAME, AttributeValue.builder().s(book.name()).build());
        if (book.stock() != null) {
            result.put(ATTRIBUTE_STOCK, AttributeValue.builder().n(String.valueOf(book.stock())).build());
        }
        result.put(ATTRIBUTE_GSI_2_PK, AttributeValue.builder().s(PREFIX_ISBN + HASH + book.isbn()).build());
        result.put(ATTRIBUTE_GSI_2_SK, result.get(ATTRIBUTE_PK));
        return result;
    }
}
