package io.micronaut.guides.core;

import io.micronaut.context.annotation.ConfigurationProperties;
import io.micronaut.starter.options.JdkVersion;
import java.util.List;
import static io.micronaut.starter.options.JdkVersion.JDK_17;
import static io.micronaut.starter.options.JdkVersion.JDK_21;

@ConfigurationProperties(GuidesConfigurationProperties.PREFIX)
public class GuidesConfigurationProperties implements GuidesConfiguration {
    public static final String PREFIX = "guides";
    public static final String GUIDES_URL = "https://guides.micronaut.io/latest/";
    private String title = "Micronaut Guides";
    private String homePageUrl = GUIDES_URL;
    private static final String DEFAULT_LICENSEHEADER = "classpath:LICENSEHEADER";
    private static final String DEFAULT_PACKAGE_NAME = "example.micronaut";
    private String licensePath = DEFAULT_LICENSEHEADER;
    private String packageName = DEFAULT_PACKAGE_NAME;
    private List<String> sourceFilesExtensions = List.of("java", "kotlin", "groovy");
    private static final String DEFAULT_APP_NAME = "default";
    private static final String HOMEPAGE_URL = "https://micronaut.io";
    private static final String LAUNCHER_URL = HOMEPAGE_URL + "/launch";
    private static final int DEFAULT_MIN_JDK = 17;
    private static final String API_URL = "https://docs.micronaut.io/latest/api";
    private static final String DEFAULT_VERSION = "classpath:version.txt";
    private static final String DEFAULT_ENV_JDK_VERSION = "JDK_VERSION";
    private static final JdkVersion DEFAULT_JAVA_VERSION = JDK_17;
    private static final List<JdkVersion> DEFAULT_JDK_VERSIONS_SUPPORTED_BY_GRAALVM = List.of(JDK_17, JDK_21);
    private String envJdkVersion = DEFAULT_ENV_JDK_VERSION;
    private JdkVersion defaulJdkVersion = DEFAULT_JAVA_VERSION;
    private List<JdkVersion> jdkVersionsSupportedByGraalvm = DEFAULT_JDK_VERSIONS_SUPPORTED_BY_GRAALVM;
    private static final String GITHUB_WORKFLOW_JAVA_CI = "Java CI";
    private static final String ENV_GITHUB_WORKFLOW = "GITHUB_WORKFLOW";
    private String githubWorkflowJavaCi = GITHUB_WORKFLOW_JAVA_CI;
    private String envGithubWorkflow = ENV_GITHUB_WORKFLOW;

    @Override
    public List<JdkVersion> getJdkVersionsSupportedByGraalvm() { return jdkVersionsSupportedByGraalvm; }

    public void setJdkVersionsSupportedByGraalvm(List<JdkVersion> jdkVersionsSupportedByGraalvm) {
        this.jdkVersionsSupportedByGraalvm = jdkVersionsSupportedByGraalvm;
    }

    public void setSourceFilesExtensions(List<String> sourceFilesExtensions) {
        this.sourceFilesExtensions = sourceFilesExtensions;
    }

    @Override
    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    @Override
    public String getLicensePath() {
        return licensePath;
    }

    public void setLicensePath(String licensePath) {
        this.licensePath = licensePath;
    }

    @Override
    public String getHomePageUrl() {
        return homePageUrl;
    }

    public void setHomePageUrl(String homePageUrl) {
        this.homePageUrl = homePageUrl;
    }

    @Override
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String getDefaultAppName() {
        return DEFAULT_APP_NAME;
    }

    @Override
    public String getProjectGeneratorUrl() { return LAUNCHER_URL; }

    @Override
    public List<String> getFilesWithHeader() {
        return sourceFilesExtensions;
    }

    @Override
    public int getDefaultMinJdk() {
        return DEFAULT_MIN_JDK;
    }

    @Override
    public String getApiUrl() {
        return API_URL;
    }

    @Override
    public String getVersionPath() {
        return DEFAULT_VERSION;
    }

    @Override
    public String getEnvJdkVersion() { return envJdkVersion; }

    public void setEnvJdkVersion(String envJdkVersion) {
        this.envJdkVersion = envJdkVersion;
    }

    @Override
    public JdkVersion getDefaultJdkVersion() { return defaulJdkVersion; }

    public void  setDefaulJdkVersion(JdkVersion jdkVersion) {
        this.defaulJdkVersion = jdkVersion;
    }

    @Override
    public String getGithubWorkflowJavaCi() { return githubWorkflowJavaCi; }

    @Override
    public void setGithubWorkflowJavaCi(String githubWorkflowJavaCi) { this.githubWorkflowJavaCi = githubWorkflowJavaCi; }

    @Override
    public String getEnvGithubWorkflow() { return envGithubWorkflow; }

    @Override
    public void setEnvGithubWorkflow(String envGithubWorkflow) { this.envGithubWorkflow = envGithubWorkflow; }

    public void setFilesWithHeader(List<String> sourceFilesExtensions) {
        this.sourceFilesExtensions = sourceFilesExtensions;
    }
}
