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
package example.micronaut

import builders.dsl.spreadsheet.api.FontStyle
import builders.dsl.spreadsheet.builder.api.CanDefineStyle
import builders.dsl.spreadsheet.builder.api.CellStyleDefinition
import builders.dsl.spreadsheet.builder.api.FontDefinition
import builders.dsl.spreadsheet.builder.api.Stylesheet

class BookExcelStylesheet : Stylesheet {

    override fun declareStyles(stylable: CanDefineStyle) {
        stylable.style(STYLE_HEADER) { st: CellStyleDefinition ->
            st.font { f: FontDefinition -> f.style(FontStyle.BOLD) }
        }
    }

    companion object {
        const val STYLE_HEADER = "header"
    }
}
