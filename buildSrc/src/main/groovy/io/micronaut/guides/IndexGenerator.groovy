package io.micronaut.guides

import groovy.json.JsonOutput
import groovy.transform.CompileDynamic
import groovy.transform.CompileStatic
import io.micronaut.core.order.OrderUtil
import io.micronaut.core.order.Ordered
import io.micronaut.guides.core.*
import io.micronaut.json.JsonMapper
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language

import java.time.format.DateTimeFormatter
import java.util.regex.Matcher
import java.util.regex.Pattern
import java.util.stream.Collectors
import java.util.stream.Stream

@CompileStatic
class IndexGenerator {

    private static final String DEFAULT_CARD = "micronauttwittercard.png"
    private static final String DEFAULT_INTRO = "Step-by-step tutorials to learn the Micronaut framework"
    private static final String DEFAULT_TITLE = "Micronaut Guides"
    private static final String GUIDES_URL = "https://micronaut-projects.github.io/micronaut-guides"
    private static final String LATEST_GUIDES_URL = GUIDES_URL + "/latest/"
    private static final String TWITTER_MICRONAUT = "@micronautfw"

    private static final Pattern CONTENT_REGEX = ~/(?s)(<main id="main">)(.*)(<\/main>)/
    public static final int NUMBER_OF_LATEST_GUIDES = 2

    private final static Comparator<Guide> GUIDE_METADATA_COMPARATOR = new Comparator<Guide>() {
        @Override
        int compare(Guide o1, Guide o2) {
            GuideUtils.getFrameworks(o1)
            int compareByFramework = GuideUtils.getFrameworks(o2).size() <=> GuideUtils.getFrameworks(o1).size()
            if (compareByFramework != 0) {
                return compareByFramework
            }
            if (o1.cloud() == null && o2.cloud() != null) {
                return -1
            }
            if (o1.cloud() != null && o2.cloud() == null) {
                return 1
            }
            if (o1.cloud() != null && o2.cloud() != null) {
                int compare = OrderUtil.COMPARATOR.compare(o1.cloud(), o2.cloud())
                if (compare != 0) {
                    return compare
                }
            }
            return o1.publicationDate() <=> o2.publicationDate()
        }
    }

    static void generateGuidesIndex(File template, File guidesFolder, File distDir, String metadataConfigName, String indexgrid) {

        //TODO. We should have an application context and get it from it.
        JsonMapper jsonMapper = JsonMapper.createDefault();
        JsonSchemaProvider jsonSchemaProvider = new DefaultJsonSchemaProvider()
        GuideParser guideParser = new DefaultGuideParser(jsonSchemaProvider, jsonMapper);
        List<Guide> metadatas = guideParser.parseGuidesMetadata(guidesFolder, metadataConfigName)
                .findAll { it.publish() }
        generateGuidesIndex(template, distDir, metadatas, indexgrid)
        GuidesConfiguration guidesConfiguration = new GuidesConfigurationProperties()
        JsonFeedConfiguration jsonFeedConfiguration = new JsonFeedConfigurationProperties()
        JsonFeedGenerator jsonFeedGenerator = new DefaultJsonFeedGenerator(guidesConfiguration, jsonFeedConfiguration, jsonMapper)
        save(distDir, jsonFeedGenerator.jsonFeedString(metadatas), jsonFeedConfiguration.getFilename())
        RssFeedGenerator rssFeedGenerator = new DefaultRssFeedGenerator(guidesConfiguration)
        RssFeedConfiguration rssFeedConfiguration = new RssFeedConfigurationProperties()
        save(distDir, rssFeedGenerator.rssFeed(metadatas), rssFeedConfiguration.getFilename())
    }

    static void generatePythonGuidesIndex(File template, File guidesFolder, File distDir, String metadataConfigName, String indexgrid) {
        //TODO. We should have an application context and get it from it.
        JsonMapper jsonMapper = JsonMapper.createDefault();
        JsonSchemaProvider jsonSchemaProvider = new DefaultJsonSchemaProvider()
        GuideParser guideParser = new DefaultGuideParser(jsonSchemaProvider, jsonMapper);
        List<Guide> metadatas = guideParser.parseGuidesMetadata(guidesFolder, metadataConfigName)
                .findAll { it.publish() && it.languages().contains(Language.PYTHON) }
        generateGuidesIndexForLanguage(template, distDir, metadatas, indexgrid, Language.PYTHON)
    }

    static void generateGuidesIndex(File template, File distDir, List<Guide> metadatas, String indexgrid) {
        generateGuidesIndexForLanguage(template, distDir, metadatas, indexgrid, null)
    }

    private static void generateGuidesIndexForLanguage(File template, File distDir, List<Guide> metadatas, String indexgrid, Language languageFilter) {
        String templateText = template.text.replaceFirst(CONTENT_REGEX) { List<String> it ->
            "${it[1]}\n    <div style=\"clear: both;\"></div><div class=\"container\">@content@</div>\n${it[3]}"
        }
        Collection<Tag> tags = collectTags(metadatas)
        for (Tag tag :  tags) {
            List<Guide> tagMetadatas = metadatas.stream()
                    .filter(m -> (GuideUtils.getTags(m) ?: []).contains(tag.slug) )
                    .sorted(GUIDE_METADATA_COMPARATOR)
                    .collect(Collectors.toList())

            Optional<Category> categoryOptional = Stream.of(Category.values()).filter(c -> tag.title ==  c.name().toLowerCase()).findFirst()

            Object cat =  categoryOptional.isPresent() ? categoryOptional.get() : tag.title

            save(templateText,
                    "tag-" + tag.slug.toLowerCase() + '.html',
                    distDir,
                    [new GuidesSection(category: cat, metadatas: tagMetadatas)],
                    tag.title,
                    null,
                    [],
                    languageFilter)
        }
        Ordered[] categories = Category.values()
        OrderUtil.sort(categories)
        List<GuidesSection> sections = []
        for (Ordered obj : categories) {
            Category cat = (Category) obj

            List<Guide> GuideList = metadatas.stream()
                    .filter(m -> m.categories().stream().anyMatch(c -> c == cat.toString()))
                    .sorted(GUIDE_METADATA_COMPARATOR)
                    .collect(Collectors.toList())


            sections << new GuidesSection(category: cat, metadatas: GuideList)
        }
        String filteredIndexGrid = languageFilter == null ? indexgrid : filterIndexGrid(indexgrid, metadatas)
        save(templateText, 'index.html', distDir, sections, 'Micronaut Guides', filteredIndexGrid, tags, languageFilter)

        for (Guide metadata :  metadatas) {
            save(templateText, metadata.slug() + '.html', distDir, [new GuidesSection(category: metadata.categories() ? metadata.categories().first() : null, metadatas: [metadata])],  metadata.title(), null, [], languageFilter)
        }
    }

    private static void save(String templateText,
                             String filename,
                             File distDir,
                             List<GuidesSection> sections,
                             String title,
                             String indexGrid,
                             Collection<Tag> tags = []) {
        save(templateText, filename, distDir, sections, title, indexGrid, tags, null)
    }

    private static void save(String templateText,
                             String filename,
                             File distDir,
                             List<GuidesSection> sections,
                             String title,
                             String indexGrid,
                             Collection<Tag> tags,
                             Language languageFilter) {
        String text = indexText(distDir, templateText, sections, tags, title, indexGrid, languageFilter)
        save(distDir, text, filename)
    }

    private static void save(File distDir, String text, String filename) {
        File output = new File(distDir, filename)
        output.createNewFile()
        output.setText(text, 'UTF-8')
    }

    private static String indexText(File distDir,
                                    String templateText,
                                    List<GuidesSection> sections,
                                    Collection<Tag> tags,
                                    String title,
                                    String indexGrid) {
        indexText(distDir, templateText, sections, tags, title, indexGrid, null)
    }

    private static String indexText(File distDir,
                                    String templateText,
                                    List<GuidesSection> sections,
                                    Collection<Tag> tags,
                                    String title,
                                    String indexGrid,
                                    Language languageFilter) {
        boolean singleGuide = sections && sections.size() == 1 && sections.get(0).metadatas.size() == 1
        List<Guide> metadatas = []
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
            index += guidesTable(latestGuides(metadatas), "Latest Guides", true, languageFilter)
            index += '    </div>'
            index += '  </div>'
            index += '</div>'
            index += '</div>'
        }

        if (indexGrid) {
            index += indexGrid
        }

        for (GuidesSection section : sections) {
            index += renderMetadatas(baseURL, section.category, section.metadatas, singleGuide, languageFilter)
        }

        String text = templateText
        if (!singleGuide && tags) {
            text = text.substring(0, text.indexOf('<div id="breadcrumbs">')) +
                    text.substring(text.indexOf('<main id="main">'))
        } else if (singleGuide) {
            String breadcrumb = '<span class="breadcrumb_last" aria-current="page">' + sections.get(0).metadatas.get(0).title() + '</span>'
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

    private static Collection<Tag> collectTags(List<Guide> metadatas) {
        Map<String, Tag> tagMap = new HashMap<>()
        for (Guide metadata : metadatas) {
            for (String slug : GuideUtils.getTags(metadata) ?: []) {
                if (tagMap.containsKey(slug)) {
                    tagMap[slug].ocurrence++
                } else {
                    tagMap[slug] = new Tag(title: slug, ocurrence: 1)
                }
            }
        }
        tagMap.values()
    }

    private static String filterIndexGrid(String indexgrid, List<Guide> metadatas) {
        Set<String> categoryIds = metadatas
                .collectMany { Guide metadata -> metadata.categories() ?: [] }
                .collect { String category ->
                    Category.values().find { Category knownCategory -> knownCategory.toString() == category }?.name()?.toLowerCase()
                }
                .findAll { String categoryId -> categoryId != null }
                .toSet()

        if (categoryIds.isEmpty()) {
            return ''
        }

        Pattern categoryItemPattern = ~/(?s)<li>\s*<h4 class="title title_small"><a href="#([^"]+)">.*?<\/li>\s*/
        Matcher categoryItemMatcher = categoryItemPattern.matcher(indexgrid)
        StringBuffer filtered = new StringBuffer()
        while (categoryItemMatcher.find()) {
            String replacement = categoryIds.contains(categoryItemMatcher.group(1)) ? categoryItemMatcher.group() : ''
            categoryItemMatcher.appendReplacement(filtered, Matcher.quoteReplacement(replacement))
        }
        categoryItemMatcher.appendTail(filtered)

        String filteredIndexGrid = filtered.toString()
        Pattern gridItemPattern = ~/<div\s+class="grid-item\b[^>]*>/
        Matcher gridItemMatcher = gridItemPattern.matcher(filteredIndexGrid)
        List<int[]> emptyGridItems = []
        while (gridItemMatcher.find()) {
            int start = gridItemMatcher.start()
            int end = matchingDivEnd(filteredIndexGrid, start)
            if (!filteredIndexGrid.substring(start, end).contains('<li>')) {
                emptyGridItems << new int[]{start, end}
            }
            gridItemMatcher.region(end, filteredIndexGrid.length())
        }
        for (int i = emptyGridItems.size() - 1; i >= 0; i--) {
            int[] range = emptyGridItems[i]
            filteredIndexGrid = filteredIndexGrid.substring(0, range[0]) + filteredIndexGrid.substring(range[1])
        }
        filteredIndexGrid
    }

    private static int matchingDivEnd(String html, int start) {
        Pattern divPattern = ~/(?s)<\/?div(?:\s[^>]*)?>/
        Matcher divMatcher = divPattern.matcher(html)
        divMatcher.region(start, html.length())
        int depth = 0
        while (divMatcher.find()) {
            if (divMatcher.group().startsWith('</')) {
                depth--
                if (depth == 0) {
                    return divMatcher.end()
                }
            } else {
                depth++
            }
        }
        html.length()
    }

    private static List<Guide> latestGuides(List<Guide> metadatas) {
        metadatas.stream()
                .distinct()
                .sorted((o1, o2) -> {
                    o2.publicationDate() <=> o1.publicationDate()
                })
                .limit(NUMBER_OF_LATEST_GUIDES)
                .collect(Collectors.toList())
    }

    static String rootUrl() {
        System.getenv("CI") ? GUIDES_URL : ''
    }

    static String twitterCardHtml(File distDir, Guide Guide) {
        String filename = new File(distDir.absolutePath, "/images/cards/" + Guide.slug() + ".png").exists() ?
                Guide.slug() + ".png" : DEFAULT_CARD
"""\
    <meta name="twitter:card" content="summary_large_image"/>
    <meta name="twitter:image" content="${rootUrl()}/latest/images/cards/${filename}"/>
    <meta name="twitter:creator" content="${TWITTER_MICRONAUT}"/>
    <meta name="twitter:site" content="${TWITTER_MICRONAUT}"/>
    <meta name="twitter:title" content="${Guide.title()?.replaceAll('"', "&quot;") ?: DEFAULT_TITLE}"/>
    <meta name="twitter:description" content="${Guide.intro()?.replaceAll('"', "&quot;") ?: DEFAULT_INTRO}"/>"""
    }

    private static String renderMetadatas(String baseURL, Object cat, List<Guide> metadatas, boolean singleGuide) {
        renderMetadatas(baseURL, cat, metadatas, singleGuide, null)
    }

    private static String renderMetadatas(String baseURL, Object cat, List<Guide> metadatas, boolean singleGuide, Language languageFilter) {
        String index = ''
        int count = 0
        List<Guide> filteredMetadatas = Utils.singleGuide() ?
                metadatas.findAll { it.slug() == Utils.singleGuide() } :
                metadatas
        if (!filteredMetadatas) {
            return index
        }
        String category = category(cat)
        String categoryId = cat instanceof Category ? ((Category) cat).name().toLowerCase() : ""
        index += '<div class="categorygrid" id="' + categoryId+ '">'
        index += "<div class='row'>"
        index += "<div class='col-sm-4'>"
        index += category
        index += "</div>"
        count++

        if (singleGuide) {
            for (Guide metadata : filteredMetadatas) {
                if ((count % 3) == 0) {
                    index += "</div>"
                    index += "<div class='row'>"
                }
                index += "<div class='col-sm-8'>"

                index += "<div class='inner'>"
                if (singleGuide) {
                    index += "<h2>${metadata.title()}</h2>"
                } else {
                    index += "<h2><a href=\"${metadata.slug()}.html\">${metadata.title()}</a></h2>"
                }
                index += "<p>${metadata.intro()}</p>"
                index += table(baseURL, metadata)
                index += "</div>"
                count++
            }
        } else {
            index += "<div class='col-sm-8'>"
            index += guidesTable(filteredMetadatas, null, false, languageFilter)
            index += "</div>"
        }

        index += "</div>"
        index += '</div>'
        index
    }

    private static String guidesTable(List<Guide> metadatas,
                                      String header = null,
                                      boolean displayPublicationDate = false) {
        guidesTable(metadatas, header, displayPublicationDate, null)
    }

    private static String guidesTable(List<Guide> metadatas,
                                      String header,
                                      boolean displayPublicationDate,
                                      Language languageFilter) {
        String index = '<div class="guide-list">'

        if (header) {
            index += "<h3 class='guide-list-header'>${header}</h3>"
        }
        Set<Cloud> clouds = []
        for (Guide metadata : metadatas) {
            if (!header) {
                if (metadata.cloud() != null && !clouds.contains(metadata.cloud())) {
                    index += "<h3 class=\"guide-list-cloud-header\"><img height=\"20\" src=\"./images/" + metadata.cloud().accronym.toLowerCase() + ".svg\" alt=\"" + metadata.cloud().name + "\"/>&nbsp;" + metadata.cloud().name + "</h3>"
                    clouds << metadata.cloud()
                }
            }
            index += '<div class="guide">'
            index += "<div class='guide-title'><a href='${guideMetadataUrl('', metadata, languageFilter)}'>${metadata.title()}</a></div>"
            if (displayPublicationDate) {
                index += "<div class='guide-date'>${metadata.publicationDate().format(DateTimeFormatter.ofPattern("MMM dd, yyyy"))}</div>"
            }
            index += "<div class='guide-intro'>${metadata.intro()}</div>"
            if (GuideUtils.getTags(metadata)?.size() > 0) {
                index += "<div class='guide-tag-list'>"
                index += "<span class='guide-tag-title'>Tags: </span>"
                GuideUtils.getTags(metadata).collect { new Tag(title: it) }.eachWithIndex { tag, i ->
                    boolean isNotLast = i != GuideUtils.getTags(metadata).size() - 1
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

    private static String guideMetadataUrl(String baseURL, Guide metadata, Language languageFilter) {
        if (languageFilter == null) {
            return "${baseURL}${metadata.slug()}.html"
        }
        GuidesOption guidesOption = GuideProjectGenerator.guidesOptions(metadata)
                .find { GuidesOption option -> option.language == languageFilter }
        guidesOption ?
                "${baseURL}${GuideProjectGenerator.folderName(metadata.slug(), guidesOption)}.html" :
                "${baseURL}${metadata.slug()}.html"
    }

    private static String guideLink(String baseURL, Guide metadata,
                                    GuidesOption guidesOption, String title = null) {
        String folder = GuideProjectGenerator.folderName(metadata.slug(), guidesOption)
        "<a href='${baseURL}${folder}.html'>${title ? title : (guidesOption.buildTool.toString() + ' - ' + guidesOption.language)}</a>"
    }

    private static String table(String baseURL, Guide metadata) {
        List<GuidesOption> guidesOptionList = GuideProjectGenerator.guidesOptions(metadata)
        String kotlinImg = '<img src="./images/kotlin.svg" width="60" alt="Kotlin"/>'
        String groovyImg = '<img src="./images/groovy.svg" width="60" alt="Groovy"/>'
        String javaImg = '<img src="./images/java.svg" width="60" alt="Java"/>'
        String pythonImg = '<img src="./images/python.png" width="60" alt="Python"/>'
        String mavenImg = '<img src="./images/maven.svg" width="60" alt="Maven"/>'
        String gradleImg = '<img src="./images/gradle.svg" width="60" alt="Gradle"/>'
        String pyronautImg = '<img src="./images/pyronaut.svg" width="100" alt="Pyronaut"/>'
        boolean hasPython = guidesOptionList.find { GuidesOption option -> option.language == Language.PYTHON } != null

        String tableHtml = """\
<table class='build-language-grid'>
<thead>
<tr>
<th></th>
"""
        tableHtml += "<th>${javaImg}</th>"
        tableHtml += "<th>${kotlinImg}</th>"
        tableHtml += "<th>${groovyImg}</th>"
        if (hasPython) {
            tableHtml += "<th>${pythonImg}</th>"
        }
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
            if (hasPython) {
                tableHtml += "<td></td>"
            }

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
            if (hasPython) {
                tableHtml += "<td></td>"
            }

            tableHtml += """\
</tr>
"""
        }
        if (guidesOptionList.find {GuidesOption option -> option.buildTool == BuildTool.PYRONAUT }) {
            tableHtml += """\
<tr>
<td>${pyronautImg}</td>
"""
            tableHtml += "<td colspan='3'></td>"
            tableHtml += cell(baseURL, metadata, BuildTool.PYRONAUT, Language.PYTHON, guidesOptionList)

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

    private static String cell(String baseURL, Guide metadata, BuildTool buildTool,
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
                case Category.VALIDATION:
                    return './images/validation.svg'
                case Category.CORE_BASICS:
                    return './images/core.svg'
                case Category.CACHE:
                    return 'https://legacy.micronaut.io/wp-content/uploads/2020/12/cache.svg'
                case Category.HTTP:
                    return './images/http.svg'
                case Category.GRAPHQL:
                    return './images/graphql.svg'
                case Category.JSON_SCHEMA:
                    return './images/json-schema.svg'
                case Category.OPEN_API:
                    return './images/openapi.svg'
                case Category.GRAALVM:
                    return './images/graalvm-mascot.svg'
                case Category.DISTRIBUTED_CONFIGURATION:
                    return './images/configuration.svg'
                case Category.METRICS:
                    return './images/metrics.svg'
                case Category.STATIC_RESOURCES:
                    return './images/static-resources.svg'
                case Category.WEBSOCKETS:
                    return './images/websockets.svg'
                case Category.SERVERLESS:
                    return './images/serverless.svg'
                case Category.DATA_MONGO:
                    return './images/mongo.svg'
                case Category.SCHEDULING:
                    return './images/scheduling.svg'
                case Category.PATTERNS:
                    return './images/patterns.svg'
                case Category.LOGGING:
                    return './images/logging.svg'
                case Category.TURBO:
                    return './images/turbo.svg'
                case Category.JAX_RS:
                    return './images/jaxrs.svg'
                case Category.DATABASE_MODELING:
                case Category.DATA_JDBC:
                case Category.DATA_JPA:
                case Category.DATA_RDBC:
                case Category.DATA_ACCESS:
                    return 'https://legacy.micronaut.io/wp-content/uploads/2020/11/dataaccess.svg'
                case Category.DEVELOPMENT:
                    return "./images/programming.svg"
                case Category.AWS_LAMBDA:
                    return "./images/lambda.svg"
                case Category.SCALE_TO_ZERO_CONTAINERS:
                    return "./images/container.svg"
                case Category.SERVICE_DISCOVERY:
                    return 'https://legacy.micronaut.io/wp-content/uploads/2020/12/Service_Discovery.svg'
                case Category.KUBERNETES:
                    return "./images/k8s.svg"
                case Category.VIEWS:
                    return "./images/html.svg"
                case Category.GRAALPY:
                    return "./images/python.svg"
                case Category.SCHEMA_MIGRATION:
                    return "https://legacy.micronaut.io/wp-content/uploads/2020/11/database-migration.svg"
                case Category.SECURITY:
                case Category.AUTHORIZATION_CODE:
                case Category.CLIENT_CREDENTIALS:
                case Category.SECRETS_MANAGER:
                    return 'https://legacy.micronaut.io/wp-content/uploads/2020/12/Security.svg'

                case Category.MESSAGING:
                    return  'https://legacy.micronaut.io/wp-content/uploads/2020/11/Messaging.svg'

                case Category.DISTRIBUTED_TRACING:
                    return 'https://legacy.micronaut.io/wp-content/uploads/2020/12/Distributed_Tracing.svg'

                case Category.OBJECT_STORAGE:
                    return './images/objectstorage.svg'

                case Category.GETTING_STARTED:
                    return 'https://legacy.micronaut.io/wp-content/uploads/2020/11/Misc.svg'

                case Category.EMAIL:
                    return 'https://legacy.micronaut.io/wp-content/uploads/2022/02/email.svg'

                case Category.TEST:
                    return './images/test.svg'
                case Category.BEYOND_JSON:
                    return './images/beyond-json.svg'

                case Category.CRAC:
                    return './images/crac.svg'
                case Category.INTERNATIONALIZATION:
                    return './images/i18n.svg'

                case Category.DISTRIBUTION:
                    return './images/distribution.svg'

                case Category.HTTP_CLIENT:
                    return './images/http-client.svg'

                case Category.KOTLIN:
                    return 'https://legacy.micronaut.io/wp-content/uploads/2021/05/Kotlin.svg'

                case Category.SPRING_BOOT_TO_MICRONAUT_BUILDING_A_REST_API:
                case Category.SPRING:
                    return './images/spring.svg'

                default:
                    return 'https://legacy.micronaut.io/wp-content/uploads/2020/11/Misc.svg'
            }
        }
        return 'https://legacy.micronaut.io/wp-content/uploads/2020/11/Misc.svg'
    }

    private static String category(Object cat) {
        String h1 = cat instanceof Category ?
                '<a href="./tag-' + cat.name().toLowerCase() + '.html\">' + cat.toString() + '</a>' :
                cat.toString().replace("_", " ")
        '<div class="category">' +
        '<div class="inner">' +
        '<img width="100" style="margin-bottom: 30px" src="' + imageForCategory(cat) + '"/>' +
        '<h1 class="title title_large first-word-bold first-word-break">' + h1 + '</h1>' +
        '<div class="underline"></div>' +
        "</div>" +
        "</div>"
    }

    static String generateGuidesJsonIndex(File guidesFolder, String metadataConfigName) {
        generateJsonIndex(guidesFolder, metadataConfigName, null)
    }

    static String generatePythonJsonIndex(File guidesFolder, String metadataConfigName) {
        generateJsonIndex(guidesFolder, metadataConfigName, Language.PYTHON)
    }

    private static String generateJsonIndex(File guidesFolder, String metadataConfigName, Language languageFilter) {
        String baseURL = languageFilter == Language.PYTHON || System.getenv("CI") ? LATEST_GUIDES_URL : ""

        //TOO get both from an application context
        JsonMapper jsonMapper = JsonMapper.createDefault();
        JsonSchemaProvider jsonSchemaProvider = new DefaultJsonSchemaProvider();
        GuideParser guideParser = new DefaultGuideParser(jsonSchemaProvider, jsonMapper);
        List<Guide> metadatas = guideParser.parseGuidesMetadata(guidesFolder, metadataConfigName)
                .findAll { it.publish() && (languageFilter == null || it.languages().contains(languageFilter)) }

        List<Map> result = metadatas
            .collect { guide ->
                List<GuidesOption> options = GuideProjectGenerator.guidesOptions(guide)
                        .findAll { option -> languageFilter == null || option.language == languageFilter }
                String guidePath = languageFilter == Language.PYTHON
                        ? "${GuideProjectGenerator.folderName(guide.slug(), options.first())}.html"
                        : "${guide.slug()}.html"
                Map guideJson = [
                        title: GuideAsciidocGenerator.postProcessText(guide.title(), languageFilter),
                        intro: GuideAsciidocGenerator.postProcessText(guide.intro(), languageFilter),
                        authors: guide.authors(),
                        tags: generateTags(guide),
                        category: guide.categories() ? guide.categories().first().toString() : null, // Deprecated
                        categories: guide.categories().collect { it.toString() },
                        publicationDate: guide.publicationDate().toString(),
                        slug: guide.slug(),
                        url: jsonUrl(baseURL, guidePath)
                ]
                if (languageFilter != Language.PYTHON) {
                    guideJson.put('options', options.collect { option -> [
                            buildTool: option.buildTool,
                            language: option.language,
                            url: jsonUrl(baseURL, "${guide.slug()}-${option.buildTool.toString().toLowerCase()}-${option.language.toString().toLowerCase()}.html")
                    ]})
                }
                guideJson
            } as List<Map>

        return JsonOutput.toJson(result)
    }

    private static String jsonUrl(String baseURL, String path) {
        baseURL ? "${baseURL.replaceAll('/+$', '')}/${path.replaceFirst('^/+', '')}" : path
    }

    @CompileDynamic
    private static List<String> generateTags(Guide guide) {
        [
            GuideUtils.getTags(guide) ?: [] +
            guide.languages*.toString() +
            guide.buildTools*.toString() +
            guide.apps.collect { GuideUtils.getAppFeatures(it,Language.DEFAULT_OPTION) }.flatten().unique()
        ]
            .flatten()
            .unique()
            as List<String>
    }

}
