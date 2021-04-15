package io.micronaut.guides

import groovy.transform.CompileStatic

@CompileStatic
class IndexGenerator {

    private static final String LATEST_GUIDES_URL = "https://micronaut-projects.github.io/micronaut-guides-poc/latest/"

    static String generateGuidesIndex(File template, File guidesFolder, String metadataConfigName) {
        String templateText = template.text
        String baseURL = System.getenv("CI") ? LATEST_GUIDES_URL : ""

        String index = ''
        List<GuideMetadata> metadatas = GuideProjectGenerator.parseGuidesMetadata(guidesFolder, metadataConfigName)
        for (GuideMetadata metadata : metadatas) {
            if (System.getProperty('micronaut.guide') != null &&
                    System.getProperty('micronaut.guide') != metadata.slug) {
                continue
            }
            List<GuidesOption> guidesOptionList = GuideProjectGenerator.guidesOptions(metadata)
            index += "<H2>${metadata.title}</H2><ul>"
            for (GuidesOption guidesOption : guidesOptionList) {
                String folder = GuideProjectGenerator.folderName(metadata.slug, guidesOption)
                index += "<li><a href='${baseURL}${folder}.html'>${guidesOption.buildTool.toString()} - ${guidesOption.language.toString()}</a></li>"
            }
            index += "</ul>"
        }
        String text = templateText
        text = text.replace("@title@", 'Micronaut Guides')
        text = text.replace("@toctitle@", 'Micronaut Guides')
        text = text.replace("@toccontent@", '')
        text = text.replace("@content@", index)

        text
    }
}
