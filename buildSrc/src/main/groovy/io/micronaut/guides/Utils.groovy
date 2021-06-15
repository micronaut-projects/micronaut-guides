package io.micronaut.guides

import io.micronaut.starter.options.JdkVersion
import org.gradle.api.GradleException

class Utils {

    private static final String SYS_PROP_MICRONAUT_GUIDE = 'micronaut.guide'
    private static final String ENV_JDK_VERSION = 'JDK_VERSION'
    private static final JdkVersion DEFAULT_JAVA_VERSION = JdkVersion.JDK_11

    static String singleGuide() {
        System.getProperty(SYS_PROP_MICRONAUT_GUIDE)
    }

    static boolean process(GuideMetadata metadata) {

        boolean processGuide = singleGuide() == null || singleGuide() == metadata.slug
        boolean jdk = metadata.minimumJavaVersion == null || parseJdkVersion().majorVersion() >= metadata.minimumJavaVersion

        return processGuide && jdk
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
