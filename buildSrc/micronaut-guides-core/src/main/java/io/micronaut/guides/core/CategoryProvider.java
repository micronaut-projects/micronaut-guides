package io.micronaut.guides.core;

public interface CategoryProvider {
    Category[] getAllCategories();
    Category findByName(String name);
}
