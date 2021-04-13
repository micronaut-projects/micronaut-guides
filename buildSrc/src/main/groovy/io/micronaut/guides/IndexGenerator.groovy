package io.micronaut.guides

import groovy.json.JsonOutput
import groovy.transform.CompileDynamic
import groovy.transform.CompileStatic

@CompileStatic
class IndexGenerator {

    private static final String LATEST_GUIDES_URL = "https://micronaut-projects.github.io/micronaut-guides-poc/latest/"

    static String generateGuidesIndex(File guidesFolder, String metadataConfigName) {
        String baseURL = System.getenv("CI") ? LATEST_GUIDES_URL : ""

        String index = '''\
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<head>
<body>
<H1>Micronaut Guides</H1>
<div>
'''
        List<GuideMetadata> metadatas = GuideProjectGenerator.parseGuidesMetadata(guidesFolder, metadataConfigName)
        for (GuideMetadata metadata : metadatas) {
            List<GuidesOption> guidesOptionList = GuideProjectGenerator.guidesOptions(metadata)
            index += "<H2>${metadata.title}</H2><ul>"
            for (GuidesOption guidesOption : guidesOptionList) {
                String folder = GuideProjectGenerator.folderName(metadata.slug, guidesOption)
                index += "<li><a href='${baseURL}${folder}.html'>${guidesOption.buildTool.toString()} - ${guidesOption.language.toString()}</a></li>"
            }
            index += "</ul>"
        }

        index += '''\
</div>
</body>
</html>
'''

        return index
    }

    static String generateGuidesJsonIndex(File guidesFolder, String metadataConfigName) {
        String baseURL = System.getenv("CI") ? LATEST_GUIDES_URL : ""

        List<GuideMetadata> metadatas = GuideProjectGenerator.parseGuidesMetadata(guidesFolder, metadataConfigName)

        List<Map> result = metadatas
            .collect {guide -> [
                title: guide.title,
                intro: guide.intro,
                authors: guide.authors,
                tags: guide.tags,
                category: guide.category,
                publicationDate: guide.publicationDate.toString(),
                slug: guide.slug,
                options: GuideProjectGenerator.guidesOptions(guide).collect {option -> [
                    buildTool: option.buildTool,
                    language: option.language,
                    url: "${baseURL}${guide.slug}-${option.buildTool.toString().toLowerCase()}-${option.language.toString().toLowerCase()}.html"
                ]}
            ]} as List<Map>

        return JsonOutput.toJson(result)
    }

}
