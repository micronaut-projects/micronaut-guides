/*
 * Copyright 2017-2023 original authors
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

import io.micronaut.scheduling.TaskExecutors
import io.micronaut.scheduling.TaskScheduler
import org.slf4j.LoggerFactory
import java.text.SimpleDateFormat
import java.time.Duration
import java.util.Date
import jakarta.inject.Named
import jakarta.inject.Singleton

@Singleton
class RegisterUseCase(private val emailUseCase: EmailUseCase, // <1>
                      @param:Named(TaskExecutors.SCHEDULED) private val taskScheduler: TaskScheduler) { // <2>

    fun register(email: String) {
        LOG.info("saving {} at {}", email, SimpleDateFormat("dd/M/yyyy hh:mm:ss").format(Date()))
        scheduleFollowupEmail(email, "Welcome to the Micronaut framework")
    }

    private fun scheduleFollowupEmail(email: String, message: String) {
        val task = EmailTask(emailUseCase, email, message) // <3>
        taskScheduler.schedule(Duration.ofMinutes(1), task)  // <4>
    }

    companion object {
        private val LOG = LoggerFactory.getLogger(RegisterUseCase::class.java)
    }
}
