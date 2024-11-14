package io.micronaut.guides.core;

import io.micronaut.context.annotation.ConfigurationProperties;
import io.micronaut.starter.options.JdkVersion;

import java.util.List;
import static io.micronaut.starter.options.JdkVersion.JDK_21;

@ConfigurationProperties(GuidesConfigurationProperties.PREFIX)
public class GuidesConfigurationProperties implements GuidesConfiguration {
    public static final String PREFIX = "guides";
    public static final String GUIDES_URL = "https://guides.micronaut.io/latest/";
    private static final String DEFAULT_LICENSEHEADER = "classpath:LICENSEHEADER";
    private static final String DEFAULT_PACKAGE_NAME = "example.micronaut";
    private static final String DEFAULT_APP_NAME = "default";
    private static final String HOMEPAGE_URL = "https://micronaut.io";
    private static final String LAUNCHER_URL = HOMEPAGE_URL + "/launch";
    private static final int DEFAULT_MIN_JDK = 21;
    private static final String API_URL = "https://docs.micronaut.io/latest/api";
    private static final String DEFAULT_VERSION = "classpath:version.txt";
    private static final String DEFAULT_ENV_JDK_VERSION = "JDK_VERSION";
    private static final JdkVersion DEFAULT_JAVA_VERSION = JDK_21;
    private static final List<JdkVersion> DEFAULT_JDK_VERSIONS_SUPPORTED_BY_GRAALVM = List.of(JDK_21);
    private static final String GITHUB_WORKFLOW_JAVA_CI = "Java CI";
    private static final String ENV_GITHUB_WORKFLOW = "GITHUB_WORKFLOW";
    private static final String SYS_PROP_MICRONAUT_GUIDE = "micronaut.guide";
    private String title = "Micronaut Guides";
    private String homePageUrl = GUIDES_URL;
    private String licensePath = DEFAULT_LICENSEHEADER;
    private String packageName = DEFAULT_PACKAGE_NAME;
    private List<String> sourceFilesExtensions = List.of("java", "kotlin", "groovy");
    private String envJdkVersion = DEFAULT_ENV_JDK_VERSION;
    private JdkVersion defaulJdkVersion = DEFAULT_JAVA_VERSION;
    private List<JdkVersion> jdkVersionsSupportedByGraalvm = DEFAULT_JDK_VERSIONS_SUPPORTED_BY_GRAALVM;
    private String githubWorkflowJavaCi = GITHUB_WORKFLOW_JAVA_CI;
    private String envGithubWorkflow = ENV_GITHUB_WORKFLOW;
    private String sysPropMicronautGuide = SYS_PROP_MICRONAUT_GUIDE;

    @Override
    public List<JdkVersion> getJdkVersionsSupportedByGraalvm() {
        return jdkVersionsSupportedByGraalvm;
    }

    public void setJdkVersionsSupportedByGraalvm(List<JdkVersion> jdkVersionsSupportedByGraalvm) {
        this.jdkVersionsSupportedByGraalvm = jdkVersionsSupportedByGraalvm;
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
    public String getProjectGeneratorUrl() {
        return LAUNCHER_URL;
    }

    @Override
    public List<String> getFilesWithHeader() {
        return sourceFilesExtensions;
    }

    public void setFilesWithHeader(List<String> sourceFilesExtensions) {
        this.sourceFilesExtensions = sourceFilesExtensions;
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
    public String getEnvJdkVersion() {
        return envJdkVersion;
    }

    public void setEnvJdkVersion(String envJdkVersion) {
        this.envJdkVersion = envJdkVersion;
    }

    @Override
    public JdkVersion getDefaultJdkVersion() {
        return defaulJdkVersion;
    }

    @Override
    public String getGithubWorkflowJavaCi() {
        return githubWorkflowJavaCi;
    }

    public void setGithubWorkflowJavaCi(String githubWorkflowJavaCi) {
        this.githubWorkflowJavaCi = githubWorkflowJavaCi;
    }

    @Override
    public String getEnvGithubWorkflow() {
        return envGithubWorkflow;
    }

    public void setEnvGithubWorkflow(String envGithubWorkflow) {
        this.envGithubWorkflow = envGithubWorkflow;
    }

    @Override
    public String getSysPropMicronautGuide() {
        return sysPropMicronautGuide;
    }

    public void setSysPropMicronautGuide(String sysPropMicronautGuide) {
        this.sysPropMicronautGuide = sysPropMicronautGuide;
    }

    public void setDefaulJdkVersion(JdkVersion jdkVersion) {
        this.defaulJdkVersion = jdkVersion;
    }

    public void setSourceFilesExtensions(List<String> sourceFilesExtensions) {
        this.sourceFilesExtensions = sourceFilesExtensions;
    }
}
