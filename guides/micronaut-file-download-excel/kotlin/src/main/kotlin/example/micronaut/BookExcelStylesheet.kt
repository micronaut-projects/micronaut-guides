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
