package example.micronaut;

import io.micronaut.context.annotation.Requires;
import io.micronaut.context.annotation.ConfigurationProperties;
import javax.validation.constraints.NotBlank;

@Requires(property = "dynamodb.table-name") // <1>
@ConfigurationProperties("dynamodb") // <2>
public interface DynamoConfiguration {
    @NotBlank
    String getTableName();
}
