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

import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.Put;
import io.micronaut.http.annotation.Status;
import io.micronaut.http.exceptions.HttpStatusException;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.annotation.ExecuteOn;

import java.util.List;

@ExecuteOn(TaskExecutors.BLOCKING) // <1>
@Controller("/tasks")
public class TaskController {

    private final TaskRepository taskRepository;

    TaskController(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Post
    @Status(HttpStatus.CREATED)
    public Task create(@Body("title") String title) { // <2>
        return taskRepository.save(new Task(null, title, false));
    }

    @Get("/open")
    public List<Task> open() {
        return taskRepository.findByCompletedFalse();
    }

    @Get("/completed")
    public List<Task> completed() {
        return taskRepository.findByCompletedTrue();
    }

    @Put("/{id}/complete")
    public Task complete(Long id) {
        if (taskRepository.updateCompleted(id, true) == 0) { // <3>
            throw new HttpStatusException(HttpStatus.NOT_FOUND, "Task not found");
        }
        return taskRepository.findById(id).orElseThrow();
    }
}
