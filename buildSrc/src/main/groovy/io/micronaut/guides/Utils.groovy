package io.micronaut.guides

import groovy.transform.CompileStatic
import io.micronaut.starter.options.JdkVersion
import org.gradle.api.GradleException

@CompileStatic
class Utils {

    private static final String SYS_PROP_MICRONAUT_GUIDE = 'micronaut.guide'
    private static final String ENV_JDK_VERSION = 'JDK_VERSION'
    private static final JdkVersion DEFAULT_JAVA_VERSION = JdkVersion.JDK_11

    static String singleGuide() {
        System.getProperty(SYS_PROP_MICRONAUT_GUIDE)
    }

    static boolean process(GuideMetadata metadata, boolean checkJdk = true) {

        if (!metadata.publish) {
            return false
        }

        boolean processGuide = singleGuide() == null || singleGuide() == metadata.slug
        if (!processGuide) {
            return false
        }

        if (checkJdk && skipBecauseOfJavaVersion(metadata)) {
            println "not processing $metadata.slug, JDK not between $metadata.minimumJavaVersion and $metadata.maximumJavaVersion"
            return false
        }

        return true
    }

    static boolean skipBecauseOfJavaVersion(GuideMetadata metadata) {
        int jdkVersion = parseJdkVersion().majorVersion()
        if ((metadata.minimumJavaVersion != null && jdkVersion < metadata.minimumJavaVersion) ||
                (metadata.maximumJavaVersion != null && jdkVersion > metadata.maximumJavaVersion)) {
            return true
        } else {
            return false
        }
    }

    static JdkVersion parseJdkVersion() {
        JdkVersion javaVersion = DEFAULT_JAVA_VERSION
        if (System.getenv(ENV_JDK_VERSION)) {
            try {
                int majorVersion = Integer.valueOf(System.getenv(ENV_JDK_VERSION))
                javaVersion = JdkVersion.valueOf(majorVersion)
            } catch (NumberFormatException ignored) {
                throw new GradleException("Could not parse env " + ENV_JDK_VERSION + " to JdkVersion")
            }
        }
        javaVersion
    }
}
