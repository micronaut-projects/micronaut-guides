package example.micronaut;

import io.micronaut.context.annotation.Requires;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.core.util.CollectionUtils;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeDefinition;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.BillingMode;
import software.amazon.awssdk.services.dynamodb.model.CreateTableRequest;
import software.amazon.awssdk.services.dynamodb.model.DeleteItemRequest;
import software.amazon.awssdk.services.dynamodb.model.DeleteItemResponse;
import software.amazon.awssdk.services.dynamodb.model.DescribeTableRequest;
import software.amazon.awssdk.services.dynamodb.model.GetItemRequest;
import software.amazon.awssdk.services.dynamodb.model.GetItemResponse;
import software.amazon.awssdk.services.dynamodb.model.GlobalSecondaryIndex;
import software.amazon.awssdk.services.dynamodb.model.KeySchemaElement;
import software.amazon.awssdk.services.dynamodb.model.KeyType;
import software.amazon.awssdk.services.dynamodb.model.Projection;
import software.amazon.awssdk.services.dynamodb.model.ProjectionType;
import software.amazon.awssdk.services.dynamodb.model.QueryRequest;
import software.amazon.awssdk.services.dynamodb.model.QueryResponse;
import software.amazon.awssdk.services.dynamodb.model.ResourceNotFoundException;
import software.amazon.awssdk.services.dynamodb.model.ScalarAttributeType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Requires(condition = CIAwsRegionProviderChainCondition.class)
@Requires(condition = CIAwsCredentialsProviderChainCondition.class)
@Requires(beans = { DynamoConfiguration.class, DynamoDbClient.class })
@Singleton
public abstract class DynamoRepository<T extends Identified> {
    private static final Logger LOG = LoggerFactory.getLogger(DynamoRepository.class);
    protected static final String HASH = "#";
    protected static final String ATTRIBUTE_PK = "pk";
    protected static final String ATTRIBUTE_SK = "sk";
    protected static final String ATTRIBUTE_GSI_1_PK = "GSI1PK";
    protected static final String ATTRIBUTE_GSI_1_SK = "GSI1SK";
    protected static final String ATTRIBUTE_GSI_2_PK = "GS21PK";
    protected static final String ATTRIBUTE_GSI_2_SK = "GS21SK";
    protected static final String INDEX_GSI_1 = "GSI1";
    protected static final String INDEX_GSI_2 = "GSI2";

    protected final DynamoDbClient dynamoDbClient;
    protected final DynamoConfiguration dynamoConfiguration;

    protected DynamoRepository(DynamoDbClient dynamoDbClient,
                            DynamoConfiguration dynamoConfiguration) {
        this.dynamoDbClient = dynamoDbClient;
        this.dynamoConfiguration = dynamoConfiguration;
    }

    public boolean existsTable() {
        try {
            dynamoDbClient.describeTable(DescribeTableRequest.builder()
                    .tableName(dynamoConfiguration.getTableName())
                    .build());
            return true;
        } catch (ResourceNotFoundException e) {
            return false;
        }
    }

    public void createTable() {
        dynamoDbClient.createTable(CreateTableRequest.builder()
                        .attributeDefinitions(AttributeDefinition.builder()
                                        .attributeName(ATTRIBUTE_PK)
                                        .attributeType(ScalarAttributeType.S)
                                        .build(),
                                AttributeDefinition.builder()
                                        .attributeName(ATTRIBUTE_SK)
                                        .attributeType(ScalarAttributeType.S)
                                        .build(),
                                AttributeDefinition.builder()
                                        .attributeName(ATTRIBUTE_GSI_1_PK)
                                        .attributeType(ScalarAttributeType.S)
                                        .build(),
                                AttributeDefinition.builder()
                                        .attributeName(ATTRIBUTE_GSI_1_SK)
                                        .attributeType(ScalarAttributeType.S)
                                        .build(),
                                AttributeDefinition.builder()
                                        .attributeName(ATTRIBUTE_GSI_2_PK)
                                        .attributeType(ScalarAttributeType.S)
                                        .build(),
                                AttributeDefinition.builder()
                                        .attributeName(ATTRIBUTE_GSI_2_SK)
                                        .attributeType(ScalarAttributeType.S)
                                        .build())
                        .keySchema(Arrays.asList(KeySchemaElement.builder()
                                .attributeName(ATTRIBUTE_PK)
                                .keyType(KeyType.HASH)
                                .build(),
                                KeySchemaElement.builder()
                                        .attributeName(ATTRIBUTE_SK)
                                        .keyType(KeyType.RANGE)
                                        .build()))
                        .billingMode(BillingMode.PAY_PER_REQUEST)
                        .tableName(dynamoConfiguration.getTableName())
                        .globalSecondaryIndexes(gsi1(), gsi2())
                .build());
    }

    @NonNull
    public QueryRequest findAllQueryRequest(@NonNull Class<?> cls,
                                            @Nullable String beforeId,
                                            @Nullable Integer limit) {
        QueryRequest.Builder builder = QueryRequest.builder()
                .tableName(dynamoConfiguration.getTableName())
                .indexName(INDEX_GSI_1)
                .scanIndexForward(false);
        if (limit != null) {
            builder.limit(limit);
        }
        if (beforeId == null) {
            return  builder.keyConditionExpression("#pk = :pk")
                    .expressionAttributeNames(Collections.singletonMap("#pk", ATTRIBUTE_GSI_1_PK))
                    .expressionAttributeValues(Collections.singletonMap(":pk",
                            classAttributeValue(cls)))
                    .build();
        } else {
            return builder.keyConditionExpression("#pk = :pk and #sk < :sk")
                    .expressionAttributeNames(CollectionUtils.mapOf("#pk", ATTRIBUTE_GSI_1_PK, "#sk", ATTRIBUTE_GSI_1_SK))
                    .expressionAttributeValues(CollectionUtils.mapOf(":pk",
                            classAttributeValue(cls),
                            ":sk",
                            id(cls, beforeId)
                    ))
                    .build();
        }
    }

    protected void delete(@NonNull @NotNull Class<?> cls, @NonNull @NotBlank String id) {
        AttributeValue pk = id(cls, id);
        DeleteItemResponse deleteItemResponse = dynamoDbClient.deleteItem(DeleteItemRequest.builder()
                .tableName(dynamoConfiguration.getTableName())
                .key(CollectionUtils.mapOf(ATTRIBUTE_PK, pk, ATTRIBUTE_SK, pk))
                .build());
        if (LOG.isDebugEnabled()) {
            LOG.debug(deleteItemResponse.toString());
        }
    }

    protected Optional<Map<String, AttributeValue>> findById(@NonNull @NotNull Class<?> cls, @NonNull @NotBlank String id) {
        AttributeValue pk = id(cls, id);
        GetItemResponse getItemResponse = dynamoDbClient.getItem(GetItemRequest.builder()
                .tableName(dynamoConfiguration.getTableName())
                .key(CollectionUtils.mapOf(ATTRIBUTE_PK, pk, ATTRIBUTE_SK, pk))
                .build());
        return !getItemResponse.hasItem() ? Optional.empty() : Optional.of(getItemResponse.item());
    }

    @NonNull
    public static Optional<String> lastEvaluatedId(@NonNull QueryResponse response,
                                          @NonNull Class<?> cls) {
        if (response.hasLastEvaluatedKey()) {
            Map<String, AttributeValue> item = response.lastEvaluatedKey();
            if (item != null && item.containsKey(ATTRIBUTE_PK)) {
                return id(cls, item.get(ATTRIBUTE_PK));
            }
        }
        return Optional.empty();
    }

    private static GlobalSecondaryIndex gsi2() {
        return gsi(INDEX_GSI_2, ATTRIBUTE_GSI_2_PK, ATTRIBUTE_GSI_2_SK);
    }

    private static GlobalSecondaryIndex gsi1() {
        return gsi(INDEX_GSI_1, ATTRIBUTE_GSI_1_PK, ATTRIBUTE_GSI_1_SK);
    }

    private static GlobalSecondaryIndex gsi(String indexName, String hashAttributeName, String rangeAttributeName) {
        return GlobalSecondaryIndex.builder()
                .indexName(indexName)
                .keySchema(KeySchemaElement.builder()
                        .attributeName(hashAttributeName)
                        .keyType(KeyType.HASH)
                        .build(), KeySchemaElement.builder()
                        .attributeName(rangeAttributeName)
                        .keyType(KeyType.RANGE)
                        .build())
                .projection(Projection.builder()
                        .projectionType(ProjectionType.ALL)
                        .build())
                .build();
    }

    @NonNull
    protected Map<String, AttributeValue> item(@NonNull T entity) {
        Map<String, AttributeValue> item = new HashMap<>();
        AttributeValue pk = id(entity.getClass(), entity.getId());
        item.put(ATTRIBUTE_PK, pk);
        item.put(ATTRIBUTE_SK, pk);
        item.put(ATTRIBUTE_GSI_1_PK, classAttributeValue(entity.getClass()));
        item.put(ATTRIBUTE_GSI_1_SK, pk);
        return item;
    }

    @NonNull
    protected static AttributeValue classAttributeValue(@NonNull Class<?> cls) {
        return AttributeValue.builder()
                .s(cls.getSimpleName())
                .build();
    }

    @NonNull
    protected static AttributeValue id(@NonNull Class<?> cls,
                                     @NonNull String id) {
        return AttributeValue.builder()
                .s(cls.getSimpleName().toUpperCase() + HASH +  id)
                .build();
    }

    @NonNull
    protected static Optional<String> id(@NonNull Class<?> cls,
                                       @NonNull AttributeValue attributeValue) {
        String str = attributeValue.s();
        String substring = cls.getSimpleName().toUpperCase() + HASH;
        return str.startsWith(substring) ? Optional.of(str.substring(substring.length())) : Optional.empty();
    }
}