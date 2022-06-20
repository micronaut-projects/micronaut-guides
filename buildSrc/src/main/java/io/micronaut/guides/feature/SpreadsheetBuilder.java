package io.micronaut.guides.feature;

import jakarta.inject.Singleton;

@Singleton
public class SpreadsheetBuilder extends AbstractFeature {

    public SpreadsheetBuilder() {
        super("spreadsheet-builder", "spreadsheet-builder-poi");
    }
}
