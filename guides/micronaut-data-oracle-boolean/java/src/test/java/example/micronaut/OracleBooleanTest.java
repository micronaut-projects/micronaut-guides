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
package example.micronaut;

import io.micronaut.core.type.Argument;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.client.BlockingHttpClient;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import io.micronaut.transaction.TransactionOperations;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@MicronautTest(transactional = false) // <1>
class OracleBooleanTest {

    @Inject
    TaskRepository taskRepository;

    @Inject
    TransactionOperations<Connection> transactionOperations;

    @Inject
    @Client("/")
    HttpClient httpClient;

    @BeforeEach
    void deleteTasks() {
        taskRepository.deleteAll();
    }

    @Test
    void completedColumnIsNativeBoolean() {
        assertEquals("BOOLEAN", columnDataType("TASK", "COMPLETED")); // <2>
    }

    @Test
    void queriesBooleanValues() {
        Task write = taskRepository.save(new Task(null, "Write the guide", false));
        Task review = taskRepository.save(new Task(null, "Review the guide", false));

        assertEquals(1, taskRepository.updateCompleted(write.id(), true)); // <3>

        assertEquals(List.of("Write the guide"), titles(taskRepository.findByCompletedTrue())); // <4>
        assertEquals(List.of("Review the guide"), titles(taskRepository.findByCompletedFalse()));
        assertEquals(List.of("Write the guide"), titles(taskRepository.findByCompleted(true))); // <5>

        assertTrue(taskRepository.findById(write.id()).orElseThrow().completed());
        assertFalse(taskRepository.findById(review.id()).orElseThrow().completed());
    }

    @Test
    void completesTaskOverHttp() {
        BlockingHttpClient client = httpClient.toBlocking();
        Task task = client.retrieve(HttpRequest.POST("/tasks", Map.of("title", "Publish the guide")), Task.class);
        assertFalse(task.completed());

        Task completed = client.retrieve(HttpRequest.PUT("/tasks/" + task.id() + "/complete", ""), Task.class); // <6>
        assertTrue(completed.completed());

        assertEquals(List.of("Publish the guide"),
            titles(client.retrieve(HttpRequest.GET("/tasks/completed"), Argument.listOf(Task.class))));
        assertTrue(client.retrieve(HttpRequest.GET("/tasks/open"), Argument.listOf(Task.class)).isEmpty());
    }

    private static List<String> titles(List<Task> tasks) {
        return tasks.stream().map(Task::title).toList();
    }

    private String columnDataType(String table, String column) {
        return transactionOperations.executeRead(status -> {
            String sql = "SELECT data_type FROM user_tab_columns WHERE table_name = ? AND column_name = ?";
            try (PreparedStatement statement = status.getConnection().prepareStatement(sql)) {
                statement.setString(1, table);
                statement.setString(2, column);
                try (ResultSet resultSet = statement.executeQuery()) {
                    return resultSet.next() ? resultSet.getString(1) : null;
                }
            } catch (SQLException e) {
                throw new IllegalStateException(e);
            }
        });
    }
}
