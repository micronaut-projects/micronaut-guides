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

import java.io.File;

public final class DiskUtils {
    private DiskUtils() {}

    public static String freeDiskSpace() {
        File root = new File("/");
        long freeBytes = root.getFreeSpace();
        double freeGB = freeBytes / (1024.0 * 1024 * 1024);
        return String.format("Free disk space: %.2f GB", freeGB);
    }
}

