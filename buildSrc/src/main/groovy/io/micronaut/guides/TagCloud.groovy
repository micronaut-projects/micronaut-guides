package io.micronaut.guides

import groovy.transform.CompileDynamic
import groovy.transform.CompileStatic
import groovy.xml.MarkupBuilder

@CompileStatic
class TagCloud {

    @CompileDynamic
    static String tagCloud(Collection<Tag> tags) {
        Integer max = tags.stream().max((o1, o2) -> {
            Integer.compare(o1.ocurrence, o2.ocurrence)
        }).get().ocurrence

        StringWriter writer = new StringWriter()
        MarkupBuilder html = new MarkupBuilder(writer)
        html.div(class: 'tagcloud', style: 'justify-content: space-around; align-items: center;') {
            tags.sort { Tag a, Tag b -> a.slug <=> b.slug }.each { Tag t ->
                a class: "tag-cloud-link ${cssClass(max, t)}", href: "./tag-${t.slug.toLowerCase()}.html", t.title
            }
        }
        writer.toString()
    }

    private static String cssClass(Integer max, Tag tag) {
        if (tag.ocurrence > ((3 * max) / 4)) {
            return 'tag-xl'
        } else if (tag.ocurrence > ((2 * max) / 4)) {
            return 'tag-lg'
        } else if (tag.ocurrence > (max / 4)) {
            return 'tag-md'
        }
        return 'tag-sm'
    }


}