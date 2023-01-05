package io.micronaut.guides

import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j

@CompileStatic
@Slf4j
class GitDiffUtils {

    static List<String> filesChanged() {
        StringBuilder sout = new StringBuilder()
        StringBuilder serr = new StringBuilder()
        Process proc = "git diff --name-only origin/master".execute()
        proc.consumeProcessOutput sout, serr
        proc.waitForOrKill 1_000
        if (serr) {
            println "error: " + serr
            return []
        }
        sout.toString().split("\n") as List<String>
    }

    static boolean onlyImagesOrMarkdownOrAsciidocChanged(List<String> filesChanged) {
        filesChanged.every { String filename ->
            filename.endsWith('adoc') ||
            filename.endsWith('markdown') ||
            filename.endsWith('md') ||
            filename.endsWith('png') ||
            filename.endsWith('jpeg') ||
            filename.endsWith('jpg') ||
            filename.endsWith('svg') ||
            filename.endsWith('gif')
        }
    }
}
