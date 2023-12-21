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
package example.micronaut;

import builders.dsl.spreadsheet.api.FontStyle;
import builders.dsl.spreadsheet.builder.api.CanDefineStyle;
import builders.dsl.spreadsheet.builder.api.Stylesheet;

public class BookExcelStylesheet implements Stylesheet {
    public static final String STYLE_HEADER = "header";

    @Override
    public void declareStyles(CanDefineStyle stylable) {
        stylable.style(STYLE_HEADER, st -> {
            st.font(f -> f.style(FontStyle.BOLD));
        });
    }
}
