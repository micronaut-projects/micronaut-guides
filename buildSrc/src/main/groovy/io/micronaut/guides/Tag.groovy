package io.micronaut.guides

import groovy.transform.Canonical
import groovy.transform.CompileStatic
import groovy.transform.Sortable

@Canonical
@Sortable(includes = ['title'])
@CompileStatic
class Tag {
    String title
    int ocurrence

    void setTitle(String title) {
        this.title = title?.trim()
    }

    String getSlug() {
        if ( !title ) {
            return null
        }
        String str = title
        str = str.toLowerCase()
        for ( String regex : [' ', '\\[', ']', '\''] ) {
            str = str.replaceAll(regex, '')
        }
        str = str.trim()
        URLEncoder.encode(str, 'UTF-8')
    }


    @Override
    public String toString() {
        return "Tag{" +
                "title='" + title + '\'' +
                ", ocurrence=" + ocurrence +
                '}';
    }
}
