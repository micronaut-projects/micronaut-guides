package io.micronaut.guides.feature.testresources;

import java.util.Locale;

public enum DbType {

    MariaDB;

    @Override
    public String toString() {
        return this.name().toLowerCase(Locale.ROOT);
    }
}
