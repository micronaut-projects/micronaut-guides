package io.micronaut.guides

import groovy.transform.CompileStatic

@CompileStatic
class IndexGenerator {

    private static final String BASE_URL = "https://micronaut-projects.github.io/micronaut-guides-poc/latest"

    static String generateGuidesIndex(File guidesFolder, String metadataConfigName) {
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
                index += "<li> <a href='${BASE_URL}/${folder}.html'>${guidesOption.buildTool.toString()} - ${guidesOption.language.toString()}</a></li>"
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
}
