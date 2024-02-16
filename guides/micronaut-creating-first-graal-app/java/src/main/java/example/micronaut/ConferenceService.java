/*
 * Copyright 2017-2024 original authors
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

import jakarta.inject.Singleton;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Singleton // <1>
public class ConferenceService {

    private static final List<Conference> CONFERENCES = Arrays.asList(
            new Conference("Greach"),
            new Conference("GR8Conf EU"),
            new Conference("Micronaut Summit"),
            new Conference("Devoxx Belgium"),
            new Conference("Oracle Code One"),
            new Conference("CommitConf"),
            new Conference("Codemotion Madrid")
    );

    public Conference randomConf() { // <2>
        return CONFERENCES.get(new Random().nextInt(CONFERENCES.size()));
    }
}
