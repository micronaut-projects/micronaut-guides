package io.micronaut.guides

class ThemeProcessor {

    static void applyThemes(File template, File dist, File guidesFolder, String metadataConfigName) {
        String templateText = template.text
        List<GuideMetadata> metadatas = GuideProjectGenerator.parseGuidesMetadata(guidesFolder, metadataConfigName)
        String tocStart = '''\
<div id="toc" class="toc2">
<div id="toctitle">Table of Contents</div>
'''
        String startDivContent ='''\
</div>
</div>
<div id="content">
'''
        String sectionbody = '<div class="sectionbody">'
        for (GuideMetadata metadata : metadatas) {
            if (!Utils.process(metadata)) {
                continue
            }
            List<GuidesOption> guidesOptionList = GuideProjectGenerator.guidesOptions(metadata)
            for (GuidesOption guidesOption : guidesOptionList) {

                String text = templateText
                String folder = GuideProjectGenerator.folderName(metadata.slug, guidesOption)
                File output = new File(dist.path, folder + ".html")
                String html = output.text

                String toc = html.indexOf(startDivContent) != -1 ? html.substring(html.indexOf(tocStart) + tocStart.length(),
                        html.indexOf(startDivContent)) : ''
                String content = html.indexOf(sectionbody) != -1 ? html.substring(html.indexOf(sectionbody) + sectionbody.length(),
                        html.indexOf('''\
</div>
</div>
</div>
</body>
''')) : ''
                content = content != '' ? ('''\
<div class="sect1">
<div class="sectionbody">
''' + content) : ''
                text = text.replace("@title@", metadata.title)
                text = text.replace("@twittercard@", IndexGenerator.twitterCardHtml(dist, metadata))
                String breadcrumb = '<a href="'+ metadata.slug +'.html">' + metadata.title + '</a> » <span class="breadcrumb_last" aria-current="page">' + guidesOption.buildTool + ' | ' + guidesOption.language  + '</span>'
                text = text.replace("@breadcrumb@", breadcrumb)
                text = text.replace("@toctitle@", 'Table of Contents')
                text = text.replace("@bodyclass@", 'guide')
                text = text.replace("@toccontent@", toc)
                text = text.replace("@content@", content)
                output.text = text
            }
        }
    }
}
