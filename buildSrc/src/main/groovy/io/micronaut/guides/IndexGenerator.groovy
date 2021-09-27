package io.micronaut.guides

import groovy.json.JsonOutput
import groovy.transform.CompileDynamic
import groovy.transform.CompileStatic
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language

import java.nio.file.Path
import java.nio.file.Paths

@CompileStatic
class IndexGenerator {

    private static final String DEFAULT_CARD = "micronauttwittercard.png";
    private static final String DEFAULT_INTRO = "Step-by-step tutorials to learn the Micronaut framework"
    private static final String DEFAULT_TITLE = "Micronaut Guides"
    private static final String GUIDES_URL = "https://guides.micronaut.io"
    private static final String LATEST_GUIDES_URL = GUIDES_URL + "/latest/"
    private static final String TWITTER_MICRONAUT = "@micronautfw"

    static void generateGuidesIndex(File template, File guidesFolder, File buildDir, String metadataConfigName) {
        List<GuideMetadata> metadatas = GuideProjectGenerator.parseGuidesMetadata(guidesFolder, metadataConfigName)

        String templateText = template.text
        templateText =
                templateText.substring(0, templateText.indexOf('''\
<main id="main">
    <div class="container">''') + '''\
<main id="main">
    <div class="container">'''.length()) +
                        '@content@' +
                        templateText.substring(templateText.indexOf('''\
    </div>
</main>'''))

        save(templateText, 'dist/index.html', buildDir, metadatas)
        for (GuideMetadata metadata :  metadatas) {
            save(templateText, "dist/${metadata.slug}.html", buildDir, [metadata], metadata.title)
        }
    }

    private static void save(String templateText,
                             String filename,
                             File buildDir,
                             List<GuideMetadata> metadatas,
                             String title = 'Micronaut Guides') {
        String text = indexText(buildDir, templateText, metadatas, title)
        Path path = Paths.get(buildDir.absolutePath, filename)
        File output = path.toFile()
        output.createNewFile()
        output.text = text
    }

    private static String indexText(File buildDir,
                                    String templateText,
                                    List<GuideMetadata> metadatas,
                                    String title) {
        boolean singleGuide = metadatas.size() == 1

        String baseURL = System.getenv("CI") ? LATEST_GUIDES_URL : ""
        String index = ''

        for (Category cat : Category.values()) {
            if (metadatas.findAll { it.category == cat }) {
                index += renderMetadatas(baseURL, cat, metadatas.findAll { it.category == cat }, singleGuide)
            }
        }
        String text = templateText
        if (!singleGuide) {
            text = text.substring(0, text.indexOf('<div id="breadcrumbs">')) +
                    text.substring(text.indexOf('<main id="main">'))
        }

        String breadcrumb = '<span class="breadcrumb_last" aria-current="page">' + metadatas.get(0).title + '</span>'
        if (singleGuide) {
            text = text.replace("@breadcrumb@", breadcrumb)
        }
        text = text.replace("@title@", title)
        String twittercard = ''
        if (singleGuide) {
            twittercard = twitterCardHtml(Paths.get(buildDir.absolutePath, "/dist").toFile(), metadatas.get(0))
        }
        text = text.replace("@twittercard@", twittercard)
        text = text.replace("@bodyclass@", 'guideindex')
        text = text.replace("@toccontent@", '')
        text = text.replace("@content@", index)
        text
    }

    static String twitterCardHtml(File distDir, GuideMetadata guideMetadata) {
        String rootUrl = System.getenv("CI") ? GUIDES_URL : ""
        String filename = new File(distDir.absolutePath, "/images/cards/" + guideMetadata.slug + ".png").exists() ?
                guideMetadata.slug + ".png" : DEFAULT_CARD
"""\
    <meta name="twitter:card" content="summary_large_image"/>
    <meta name="twitter:image" content="${rootUrl}/latest/images/cards/${filename}"/>
    <meta name="twitter:creator" content="${TWITTER_MICRONAUT}"/>
    <meta name="twitter:site" content="${TWITTER_MICRONAUT}"/>
    <meta name="twitter:title" content="${guideMetadata.title?.replaceAll('"', "&quot;") ?: DEFAULT_TITLE}"/>
    <meta name="twitter:description" content="${guideMetadata.intro?.replaceAll('"', "&quot;") ?: DEFAULT_INTRO}"/>"""
    }

    private static String renderMetadatas(String baseURL, Category cat, List<GuideMetadata> metadatas, boolean singleGuide) {
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

        for (GuideMetadata metadata : filteredMetadatas) {
            if ((count % 3) == 0) {
                index += "</div>"
                index += "<div class='row'>"
            }


            if (singleGuide) {
                index += "<div class='col-sm-8'>"
            } else {
                index += "<div class='col-sm-4'>"
            }

            index += "<div class='inner'>"
            if (singleGuide) {
                index += "<h2>${metadata.title}</h2>"
            } else {
                index += "<h2><a href=\"${metadata.slug}.html\">${metadata.title}</a></h2>"
            }
            index += "<p>${metadata.intro}</p>"
            index += table(baseURL, metadata)
            index += "</div>"
            index += "</div>"

            count ++
        }

        index += "</div>"
        index += '</div>'
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

    private static String imageForCategory(Category cat) {
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
                return 'https://micronaut.io/wp-content/uploads/2020/12/Security.svg'

            case Category.MESSAGING:
                return  'https://micronaut.io/wp-content/uploads/2020/11/Messaging.svg'

            case Category.DISTRIBUTED_TRACING:
                return 'https://micronaut.io/wp-content/uploads/2020/12/Distributed_Tracing.svg'

            case Category.GETTING_STARTED:
                return 'https://micronaut.io/wp-content/uploads/2020/11/Misc.svg'

            case Category.ORACLE_CLOUD:
                return 'https://micronaut.io/wp-content/uploads/2021/05/Oracle-1.svg'

            default:
                return 'https://micronaut.io/wp-content/uploads/2020/11/Misc.svg'
        }

    }

    private static String category(Category cat) {
        String html = "<div class='category'>"
        html += '<div class="inner">'
        html += '<img width="100" style="margin-bottom: 30px" src="' + imageForCategory(cat) + '"/>'
        html += '<h1 class="title title_large first-word-bold first-word-break">' + cat  + '</h1>'
        html += '<div class="underline"></div>'
        html += "</div>"
        html += "</div>"
        html
    }

    static String generateGuidesJsonIndex(File guidesFolder, String metadataConfigName) {
        String baseURL = System.getenv("CI") ? LATEST_GUIDES_URL : ""

        List<GuideMetadata> metadatas = GuideProjectGenerator.parseGuidesMetadata(guidesFolder, metadataConfigName)

        List<Map> result = metadatas
            .collect {guide -> [
                title: guide.title,
                intro: guide.intro,
                authors: guide.authors,
                tags: generateTags(guide),
                category: guide.category.toString(),
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
            guide.tags +
            guide.languages*.toString() +
            guide.buildTools*.toString() +
            guide.apps.collect { it.features }.flatten().unique()
        ]
            .flatten()
            .unique()
            as List<String>
    }

}
