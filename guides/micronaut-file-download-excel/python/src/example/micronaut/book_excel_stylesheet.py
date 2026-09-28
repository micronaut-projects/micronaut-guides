from builders.dsl.spreadsheet.api import FontStyle


class BookExcelStylesheet:
    STYLE_HEADER = "header"

    def declareStyles(self, stylable):
        stylable.style(
            self.STYLE_HEADER,
            lambda style: style.font(lambda font: font.style(FontStyle.BOLD)),
        )
