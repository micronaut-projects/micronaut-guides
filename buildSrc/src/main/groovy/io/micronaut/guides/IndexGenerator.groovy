package io.micronaut.guides

import groovy.json.JsonOutput
import groovy.transform.CompileDynamic
import groovy.transform.CompileStatic
import io.micronaut.core.order.OrderUtil
import io.micronaut.core.order.Ordered
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language

import java.time.format.DateTimeFormatter
import java.util.regex.Pattern
import java.util.stream.Collectors

@CompileStatic
class IndexGenerator {

    private static final String DEFAULT_CARD = "micronauttwittercard.png"
    private static final String DEFAULT_INTRO = "Step-by-step tutorials to learn the Micronaut framework"
    private static final String DEFAULT_TITLE = "Micronaut Guides"
    private static final String GUIDES_URL = "https://guides.micronaut.io"
    private static final String LATEST_GUIDES_URL = GUIDES_URL + "/latest/"
    private static final String TWITTER_MICRONAUT = "@micronautfw"

    private static final Pattern CONTENT_REGEX = ~/(?s)(<main id="main">)(.*)(<\/main>)/
    public static final int NUMBER_OF_LATEST_GUIDES = 4

    static void generateGuidesIndex(File template, File guidesFolder, File distDir, String metadataConfigName) {

        List<GuideMetadata> metadatas = GuideProjectGenerator.parseGuidesMetadata(guidesFolder, metadataConfigName)
                .findAll { it.publish }
        generateGuidesIndex(template, distDir, metadatas)
    }

    static void generateGuidesIndex(File template, File distDir, List<GuideMetadata> metadatas) {
        String templateText = template.text.replaceFirst(CONTENT_REGEX) { List<String> it ->
            "${it[1]}\n    <div class=\"container\">@content@</div>\n${it[3]}"
        }
        Collection<Tag> tags = collectTags(metadatas)
        for (Tag tag :  tags) {
            List<GuideMetadata> tagMetadatas = metadatas.stream()
                    .filter(m -> (m.tags ?: []).contains(tag.slug) )
                    .collect(Collectors.toList())
            save(templateText,
                    "tag-" + tag.slug.toLowerCase() + '.html',
                    distDir,
                    [new GuidesSection(category: tag.title, metadatas: tagMetadatas)],
                    tag.title)
        }
        Ordered[] categories = Category.values()
        OrderUtil.sort(categories)
        List<GuidesSection> sections = []
        for (Ordered obj : categories) {
            Category cat = (Category) obj
            sections << new GuidesSection(category: cat,
                    metadatas: metadatas.stream().filter(m -> m.getCategories().stream().anyMatch(c -> c == cat)).collect(Collectors.toList()))
        }
        save(templateText, 'index.html', distDir, sections, 'Micronaut Guides', tags)

        for (GuideMetadata metadata :  metadatas) {
            save(templateText, metadata.slug + '.html', distDir, [new GuidesSection(category: metadata.categories ? metadata.categories.first() : null, metadatas: [metadata])],  metadata.title)
        }
    }

    private static void save(String templateText,
                             String filename,
                             File distDir,
                             List<GuidesSection> sections,
                             String title,
                             Collection<Tag> tags = []) {
        String text = indexText(distDir, templateText, sections, tags, title)
        File output = new File(distDir, filename)
        output.createNewFile()
        output.setText(text, 'UTF-8')
    }

    private static String indexText(File distDir,
                                    String templateText,
                                    List<GuidesSection> sections,
                                    Collection<Tag> tags,
                                    String title) {
        boolean singleGuide = sections && sections.size() == 1 && sections.get(0).metadatas.size() == 1
        List<GuideMetadata> metadatas = []
        for (GuidesSection section : sections) {
            metadatas.addAll(section.metadatas)
        }

        String baseURL = System.getenv("CI") ? LATEST_GUIDES_URL : ""
        String index = ''
        if (!singleGuide && tags) {
            index += '<div class="categorygrid">'
            index += '<div class="grid">'
            index += '  <div class="grid-item grid-item_primary grid-item_one-third">'
            index += '    <div class="inner">'
            index += '      <h1 class="title title_large first-word-bold first-word-break"><strong>Micronaut</strong> Guides</h1>'
            index += '    </div>'
            index += '  </div>'
            index += '  <div class="grid-item grid-item_white grid-item_two-third grid-item_dynamic-height latest-guides">'
            index += '    <div class="inner" style="padding: 0">'
            index += guidesTable(latestGuides(metadatas), "Latest Guides", true)
            index += '    </div>'
            index += '  </div>'
            index += '</div>'
            index += '</div>'
        }

        for (GuidesSection section : sections) {
            index += renderMetadatas(baseURL, section.category, section.metadatas, singleGuide)
        }

        String text = templateText
        if (!singleGuide && tags) {
            text = text.substring(0, text.indexOf('<div id="breadcrumbs">')) +
                    text.substring(text.indexOf('<main id="main">'))
        } else if (singleGuide) {
            String breadcrumb = '<span class="breadcrumb_last" aria-current="page">' + sections.get(0).metadatas.get(0).title + '</span>'
            text = text.replace("@breadcrumb@", breadcrumb)
        } else if (!singleGuide && !tags) {
            String breadcrumb = '<span class="breadcrumb_last" aria-current="page">' + sections.get(0).category.toString() + '</span>'
            text = text.replace("@breadcrumb@", breadcrumb)
        }
        text = text.replace("@title@", title.charAt(0).toUpperCase().toString() + title.substring(1) + " | Micronaut Guides | Micronaut Framework")
        String twittercard = ''
        if (singleGuide) {
            twittercard = twitterCardHtml(distDir, sections.get(0).metadatas.get(0))
        }
        text = text.replace("@twittercard@", twittercard)
        text = text.replace("@bodyclass@", 'guideindex')
        text = text.replace("@toccontent@", '')
        text = text.replace("@content@", index)
        text
    }

    private static Collection<Tag> collectTags(List<GuideMetadata> metadatas) {
        Map<String, Tag> tagMap = new HashMap<>()
        for (GuideMetadata metadata : metadatas) {
            for (String slug : metadata.getTags() ?: []) {
                if (tagMap.containsKey(slug)) {
                    tagMap[slug].ocurrence++
                } else {
                    tagMap[slug] = new Tag(title: slug, ocurrence: 1)
                }
            }
        }
        tagMap.values()
    }

    private static List<GuideMetadata> latestGuides(List<GuideMetadata> metadatas) {
        metadatas.stream()
                .distinct()
                .sorted((o1, o2) -> {
                    o2.publicationDate <=> o1.publicationDate
                })
                .limit(NUMBER_OF_LATEST_GUIDES)
                .collect(Collectors.toList())
    }

    static String rootUrl() {
        System.getenv("CI") ? GUIDES_URL : ''
    }

    static String twitterCardHtml(File distDir, GuideMetadata guideMetadata) {
        String filename = new File(distDir.absolutePath, "/images/cards/" + guideMetadata.slug + ".png").exists() ?
                guideMetadata.slug + ".png" : DEFAULT_CARD
"""\
    <meta name="twitter:card" content="summary_large_image"/>
    <meta name="twitter:image" content="${rootUrl()}/latest/images/cards/${filename}"/>
    <meta name="twitter:creator" content="${TWITTER_MICRONAUT}"/>
    <meta name="twitter:site" content="${TWITTER_MICRONAUT}"/>
    <meta name="twitter:title" content="${guideMetadata.title?.replaceAll('"', "&quot;") ?: DEFAULT_TITLE}"/>
    <meta name="twitter:description" content="${guideMetadata.intro?.replaceAll('"', "&quot;") ?: DEFAULT_INTRO}"/>"""
    }

    private static String renderMetadatas(String baseURL, Object cat, List<GuideMetadata> metadatas, boolean singleGuide) {
        String index = ''
        int count = 0
        List<GuideMetadata> filteredMetadatas = Utils.singleGuide() ?
                metadatas.findAll { it.slug == Utils.singleGuide() } :
                metadatas
        if (!filteredMetadatas) {
            return index
        }
        index += '<div class="categorygrid">'
        index += "<div class='row'>"
        index += "<div class='col-sm-4'>"
        index += category(cat)
        index += "</div>"
        count++

        if (singleGuide) {
            for (GuideMetadata metadata : filteredMetadatas) {
                if ((count % 3) == 0) {
                    index += "</div>"
                    index += "<div class='row'>"
                }
                index += "<div class='col-sm-8'>"

                index += "<div class='inner'>"
                if (singleGuide) {
                    index += "<h2>${metadata.title}</h2>"
                } else {
                    index += "<h2><a href=\"${metadata.slug}.html\">${metadata.title}</a></h2>"
                }
                index += "<p>${metadata.intro}</p>"
                index += table(baseURL, metadata)
                index += "</div>"
                count++
            }
        } else {
            index += "<div class='col-sm-8'>"
            index += guidesTable(filteredMetadatas)
            index += "</div>"
        }

        index += "</div>"
        index += '</div>'
        index
    }

    private static String guidesTable(List<GuideMetadata> metadatas,
                                      String header = null,
                                      boolean displayPublicationDate = false) {
        String index = '<div class="guide-list">'

        if (header) {
            index += "<h3 class='guide-list-header'>${header}</h3>"
        }
        for (GuideMetadata metadata : metadatas) {
            index += '<div class="guide">'
            index += "<div class='guide-title'><a href='${metadata.slug}.html'>${metadata.title}</a></div>"
            if (displayPublicationDate) {
                index += "<div class='guide-date'>${metadata.publicationDate.format(DateTimeFormatter.ofPattern("MMM dd, yyyy"))}</div>"
            }
            index += "<div class='guide-intro'>${metadata.intro}</div>"
            if (metadata.tags?.size() > 0) {
                index += "<div class='guide-tag-list'>"
                index += "<span class='guide-tag-title'>Tags: </span>"
                metadata.tags.collect { new Tag(title: it) }.eachWithIndex { tag, i ->
                    boolean isNotLast = i != metadata.tags.size() - 1
                    index += "<span class='guide-tag'><a href='./tag-${tag.slug.toLowerCase()}.html'>${tag.title}</a></span>"
                    if (isNotLast) {
                        index += "<span class='guide-split'>, </span>"
                    }
                }
                index += '</div>'
            }
            index += "</div>"
        }
        index += "</div>"
        index
    }

    private static String guideLink(String baseURL, GuideMetadata metadata,
                                    GuidesOption guidesOption, String title = null) {
        String folder = GuideProjectGenerator.folderName(metadata.slug, guidesOption)
        "<a href='${baseURL}${folder}.html'>${title ? title : (guidesOption.buildTool.toString() + ' - ' + guidesOption.language)}</a>"
    }

    private static String table(String baseURL, GuideMetadata metadata) {
        List<GuidesOption> guidesOptionList = GuideProjectGenerator.guidesOptions(metadata)
        String kotlinImg = '<img src="./images/kotlin.svg" width="60" alt="Kotlin"/>'
        String groovyImg = '<img src="./images/groovy.svg" width="60" alt="Groovy"/>'
        String javaImg = '<img src="./images/java.svg" width="60" alt="Java"/>'
        String mavenImg = '<img src="./images/maven.svg" width="60" alt="Maven"/>'
        String gradleImg = '<img src="./images/gradle.svg" width="60" alt="Gradle"/>'

        String tableHtml = """\
<table class='build-language-grid'>
<thead>
<tr>
<th></th>
"""
        tableHtml += "<th>${javaImg}</th>"
        tableHtml += "<th>${kotlinImg}</th>"
        tableHtml += "<th>${groovyImg}</th>"
        tableHtml += """\
</tr>
</thead>
<tbody>
"""
        if (guidesOptionList.find {GuidesOption option -> option.buildTool == BuildTool.GRADLE }) {
            tableHtml += """\
<tr>
<td>${gradleImg}</td>
"""

            tableHtml += cell(baseURL, metadata, BuildTool.GRADLE, Language.JAVA, guidesOptionList)
            tableHtml += cell(baseURL, metadata, BuildTool.GRADLE, Language.KOTLIN, guidesOptionList)
            tableHtml += cell(baseURL, metadata, BuildTool.GRADLE, Language.GROOVY, guidesOptionList)

            tableHtml += """\
</tr>
"""
        }
        if (guidesOptionList.find {GuidesOption option -> option.buildTool == BuildTool.MAVEN }) {
            tableHtml += """\
<tr>
<td>${mavenImg}</td>
"""
            tableHtml += cell(baseURL, metadata, BuildTool.MAVEN, Language.JAVA, guidesOptionList)
            tableHtml += cell(baseURL, metadata, BuildTool.MAVEN, Language.KOTLIN, guidesOptionList)
            tableHtml += cell(baseURL, metadata, BuildTool.MAVEN, Language.GROOVY, guidesOptionList)

            tableHtml += """\
</tr>
"""
        }
        tableHtml += """\
</tbody>
</table>
"""
        tableHtml
    }

    private static String cell(String baseURL, GuideMetadata metadata, BuildTool buildTool,
                               Language language, List<GuidesOption> guidesOptionList) {
        String tableHtml = "<td>"
        GuidesOption guidesOption = guidesOptionList.find {GuidesOption option -> option.buildTool == buildTool&& option.language == language }
        if (guidesOption) {
            tableHtml += guideLink(baseURL, metadata , guidesOption, "Read")
        } else {
            tableHtml += "<span></span>"
        }
        tableHtml += "</td>"
        tableHtml
    }

    private static String imageForCategory(Object obj) {
        if (obj instanceof Category) {
            Category cat = (Category)  obj
            switch (cat) {
                case Category.GCP:
                    return 'https://micronaut.io/wp-content/uploads/2021/02/Googlecloud.svg'

                case Category.AWS:
                    return 'https://micronaut.io/wp-content/uploads/2020/12/aws.svg'

                case Category.AZURE:
                    return 'https://micronaut.io/wp-content/uploads/2020/12/Azure.svg'

                case Category.CACHE:
                    return 'https://micronaut.io/wp-content/uploads/2020/12/cache.svg'

                case Category.DATA_ACCESS:
                    return 'https://micronaut.io/wp-content/uploads/2020/11/dataaccess.svg'

                case Category.SERVICE_DISCOVERY:
                    return 'https://micronaut.io/wp-content/uploads/2020/12/Service_Discovery.svg'

                case Category.SECURITY:
                case Category.AUTHORIZATION_CODE:
                case Category.CLIENT_CREDENTIALS:
                case Category.SECRETS_MANAGER:
                    return 'https://micronaut.io/wp-content/uploads/2020/12/Security.svg'

                case Category.MESSAGING:
                    return  'https://micronaut.io/wp-content/uploads/2020/11/Messaging.svg'

                case Category.DISTRIBUTED_TRACING:
                    return 'https://micronaut.io/wp-content/uploads/2020/12/Distributed_Tracing.svg'

                case Category.GETTING_STARTED:
                    return 'https://micronaut.io/wp-content/uploads/2020/11/Misc.svg'

                case Category.ORACLE_CLOUD:
                    return 'https://micronaut.io/wp-content/uploads/2021/05/Oracle-1.svg'

                case Category.API:
                    return 'https://micronaut.io/wp-content/uploads/2020/11/API.svg'

                case Category.EMAIL:
                    return 'https://micronaut.io/wp-content/uploads/2022/02/email.svg'

                case Category.TEST:
                    return 'https://micronaut.io/wp-content/uploads/2020/11/Build.svg'

                case Category.KOTLIN:
                    return 'https://micronaut.io/wp-content/uploads/2021/05/Kotlin.svg'

                default:
                    return 'https://micronaut.io/wp-content/uploads/2020/11/Misc.svg'
            }
        }
        return 'https://micronaut.io/wp-content/uploads/2020/11/Misc.svg'
    }

    private static String category(cat) {
        "<div class='category'>" +
        '<div class="inner">' +
        '<img width="100" style="margin-bottom: 30px" src="' + imageForCategory(cat) + '"/>' +
        '<h1 class="title title_large first-word-bold first-word-break">' + cat + '</h1>' +
        '<div class="underline"></div>' +
        "</div>" +
        "</div>"
    }

    static String generateGuidesJsonIndex(File guidesFolder, String metadataConfigName) {
        String baseURL = System.getenv("CI") ? LATEST_GUIDES_URL : ""

        List<GuideMetadata> metadatas = GuideProjectGenerator.parseGuidesMetadata(guidesFolder, metadataConfigName)
                .findAll { it.publish }

        List<Map> result = metadatas
            .collect {guide -> [
                title: guide.title,
                intro: guide.intro,
                authors: guide.authors,
                tags: generateTags(guide),
                category: guide.categories ? guide.categories.first().toString() : null, // Deprecated
                categories: guide.categories.collect { it.toString() },
                publicationDate: guide.publicationDate.toString(),
                slug: guide.slug,
                url: "${baseURL}${guide.slug}.html",
                options: GuideProjectGenerator.guidesOptions(guide).collect {option -> [
                    buildTool: option.buildTool,
                    language: option.language,
                    url: "${baseURL}${guide.slug}-${option.buildTool.toString().toLowerCase()}-${option.language.toString().toLowerCase()}.html"
                ]}
            ]} as List<Map>

        return JsonOutput.toJson(result)
    }

    @CompileDynamic
    private static List<String> generateTags(GuideMetadata guide) {
        [
            guide.tags ?: [] +
            guide.languages*.toString() +
            guide.buildTools*.toString() +
            guide.apps.collect { it.getFeatures(Language.DEFAULT_OPTION) }.flatten().unique()
        ]
            .flatten()
            .unique()
            as List<String>
    }

}
