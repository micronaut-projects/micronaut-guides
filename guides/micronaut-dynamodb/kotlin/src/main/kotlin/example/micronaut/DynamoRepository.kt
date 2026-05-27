/*
 * Copyright 2017-2026 original authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package example.micronaut

import io.micronaut.context.annotation.Primary
import io.micronaut.context.annotation.Requires
import jakarta.inject.Singleton
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import org.slf4j.LoggerFactory
import software.amazon.awssdk.services.dynamodb.DynamoDbClient
import software.amazon.awssdk.services.dynamodb.model.AttributeDefinition
import software.amazon.awssdk.services.dynamodb.model.AttributeValue
import software.amazon.awssdk.services.dynamodb.model.BillingMode
import software.amazon.awssdk.services.dynamodb.model.CreateTableRequest
import software.amazon.awssdk.services.dynamodb.model.DeleteItemRequest
import software.amazon.awssdk.services.dynamodb.model.DescribeTableRequest
import software.amazon.awssdk.services.dynamodb.model.GetItemRequest
import software.amazon.awssdk.services.dynamodb.model.GlobalSecondaryIndex
import software.amazon.awssdk.services.dynamodb.model.KeySchemaElement
import software.amazon.awssdk.services.dynamodb.model.KeyType
import software.amazon.awssdk.services.dynamodb.model.Projection
import software.amazon.awssdk.services.dynamodb.model.ProjectionType
import software.amazon.awssdk.services.dynamodb.model.QueryRequest
import software.amazon.awssdk.services.dynamodb.model.QueryResponse
import software.amazon.awssdk.services.dynamodb.model.ResourceNotFoundException
import software.amazon.awssdk.services.dynamodb.model.ScalarAttributeType
import java.util.Optional

@Requires(condition = CIAwsRegionProviderChainCondition::class)
@Requires(condition = CIAwsCredentialsProviderChainCondition::class)
@Requires(beans = [DynamoConfiguration::class, DynamoDbClient::class])
@Singleton
@Primary
open class DynamoRepository<T : Identified>(
    protected val dynamoDbClient: DynamoDbClient,
    protected val dynamoConfiguration: DynamoConfiguration
) {

    fun existsTable(): Boolean =
        try {
            dynamoDbClient.describeTable(
                DescribeTableRequest.builder()
                    .tableName(dynamoConfiguration.tableName)
                    .build()
            )
            true
        } catch (e: ResourceNotFoundException) {
            false
        }

    fun createTable() {
        dynamoDbClient.createTable(
            CreateTableRequest.builder()
                .attributeDefinitions(
                    AttributeDefinition.builder()
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
                        .build()
                )
                .keySchema(
                    KeySchemaElement.builder()
                        .attributeName(ATTRIBUTE_PK)
                        .keyType(KeyType.HASH)
                        .build(),
                    KeySchemaElement.builder()
                        .attributeName(ATTRIBUTE_SK)
                        .keyType(KeyType.RANGE)
                        .build()
                )
                .billingMode(BillingMode.PAY_PER_REQUEST)
                .tableName(dynamoConfiguration.tableName)
                .globalSecondaryIndexes(gsi1())
                .build()
        )
    }

    open fun findAllQueryRequest(
        cls: Class<*>,
        beforeId: String?,
        limit: Int?
    ): QueryRequest {
        val builder = QueryRequest.builder()
            .tableName(dynamoConfiguration.tableName)
            .indexName(INDEX_GSI_1)
            .scanIndexForward(false)
        if (limit != null) {
            builder.limit(limit)
        }
        return if (beforeId == null) {
            builder.keyConditionExpression("#pk = :pk")
                .expressionAttributeNames(mapOf("#pk" to ATTRIBUTE_GSI_1_PK))
                .expressionAttributeValues(mapOf(":pk" to classAttributeValue(cls)))
                .build()
        } else {
            builder.keyConditionExpression("#pk = :pk and #sk < :sk")
                .expressionAttributeNames(mapOf("#pk" to ATTRIBUTE_GSI_1_PK, "#sk" to ATTRIBUTE_GSI_1_SK))
                .expressionAttributeValues(mapOf(":pk" to classAttributeValue(cls), ":sk" to id(cls, beforeId)))
                .build()
        }
    }

    protected open fun delete(@NotNull cls: Class<*>, @NotBlank id: String) {
        val pk = id(cls, id)
        val deleteItemResponse = dynamoDbClient.deleteItem(
            DeleteItemRequest.builder()
                .tableName(dynamoConfiguration.tableName)
                .key(mapOf(ATTRIBUTE_PK to pk, ATTRIBUTE_SK to pk))
                .build()
        )
        if (LOG.isDebugEnabled) {
            LOG.debug(deleteItemResponse.toString())
        }
    }

    protected open fun findById(@NotNull cls: Class<*>, @NotBlank id: String): Optional<Map<String, AttributeValue>> {
        val pk = id(cls, id)
        val getItemResponse = dynamoDbClient.getItem(
            GetItemRequest.builder()
                .tableName(dynamoConfiguration.tableName)
                .key(mapOf(ATTRIBUTE_PK to pk, ATTRIBUTE_SK to pk))
                .build()
        )
        return if (!getItemResponse.hasItem()) Optional.empty() else Optional.of(getItemResponse.item())
    }

    protected open fun item(entity: T): MutableMap<String, AttributeValue> {
        val item = mutableMapOf<String, AttributeValue>()
        val pk = id(entity.javaClass, entity.id)
        item[ATTRIBUTE_PK] = pk
        item[ATTRIBUTE_SK] = pk
        item[ATTRIBUTE_GSI_1_PK] = classAttributeValue(entity.javaClass)
        item[ATTRIBUTE_GSI_1_SK] = pk
        return item
    }

    companion object {
        private val LOG = LoggerFactory.getLogger(DynamoRepository::class.java)
        private const val HASH = "#"
        private const val ATTRIBUTE_PK = "pk"
        private const val ATTRIBUTE_SK = "sk"
        private const val ATTRIBUTE_GSI_1_PK = "GSI1PK"
        private const val ATTRIBUTE_GSI_1_SK = "GSI1SK"
        private const val INDEX_GSI_1 = "GSI1"

        fun lastEvaluatedId(response: QueryResponse, cls: Class<*>): Optional<String> {
            if (response.hasLastEvaluatedKey()) {
                val item = response.lastEvaluatedKey()
                if (item != null && item.containsKey(ATTRIBUTE_PK)) {
                    return id(cls, item.getValue(ATTRIBUTE_PK))
                }
            }
            return Optional.empty()
        }

        private fun gsi1(): GlobalSecondaryIndex =
            GlobalSecondaryIndex.builder()
                .indexName(INDEX_GSI_1)
                .keySchema(
                    KeySchemaElement.builder()
                        .attributeName(ATTRIBUTE_GSI_1_PK)
                        .keyType(KeyType.HASH)
                        .build(),
                    KeySchemaElement.builder()
                        .attributeName(ATTRIBUTE_GSI_1_SK)
                        .keyType(KeyType.RANGE)
                        .build()
                )
                .projection(
                    Projection.builder()
                        .projectionType(ProjectionType.ALL)
                        .build()
                )
                .build()

        private fun classAttributeValue(cls: Class<*>): AttributeValue =
            AttributeValue.builder()
                .s(cls.simpleName)
                .build()

        private fun id(cls: Class<*>, id: String): AttributeValue =
            AttributeValue.builder()
                .s(listOf(cls.simpleName.uppercase(), id).joinToString(HASH))
                .build()

        private fun id(cls: Class<*>, attributeValue: AttributeValue): Optional<String> {
            val str = attributeValue.s()
            val substring = cls.simpleName.uppercase() + HASH
            return if (str.startsWith(substring)) Optional.of(str.substring(substring.length)) else Optional.empty()
        }
    }
}
