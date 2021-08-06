package io.micronaut.guides.feature;

import javax.inject.Singleton;

@Singleton
public class SpreadsheetBuilder extends AbstractFeature {

    public SpreadsheetBuilder() {
        super("spreadsheet-builder", "spreadsheet-builder-poi");
    }
}
