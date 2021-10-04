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
