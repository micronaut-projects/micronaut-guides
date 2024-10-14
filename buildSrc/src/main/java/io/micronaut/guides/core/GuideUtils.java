package io.micronaut.guides.core;

import java.util.*;
import java.util.stream.Collectors;

public final class GuideUtils {

    private static final String MICRONAUT_PREFIX = "micronaut-";
    private static final String VIEWS_PREFIX = "views-";
    private static final List<String> FEATURES_PREFIXES = List.of(MICRONAUT_PREFIX, VIEWS_PREFIX);

    private GuideUtils() {}

    public static List<String> getTags(Guide guide) {
        Set<String> tagsList = new HashSet<>();
        if (guide.tags() != null) {
            addAllSafe(tagsList, guide.tags());
        }
        for (App app : guide.apps()) {
            List<String> allFeatures = new ArrayList<>();
            addAllSafe(allFeatures,app.features());
            addAllSafe(allFeatures,app.javaFeatures());
            addAllSafe(allFeatures,app.kotlinFeatures());
            addAllSafe(allFeatures,app.groovyFeatures());
            for (String featureName : allFeatures) {
                String tagToAdd = featureName;
                for (String prefix : FEATURES_PREFIXES) {
                    if (tagToAdd.startsWith(prefix)) {
                        tagToAdd = tagToAdd.substring(prefix.length());
                    }
                }
                tagsList.add(tagToAdd);
            }
        }
        Set<String> categoriesAsTags = guide.categories().stream().map(String::toLowerCase).map(s -> s.replace(" ","-")).collect(Collectors.toSet());
        tagsList.addAll(categoriesAsTags);
        return tagsList.stream().collect(Collectors.toList());
    }

    public static Guide merge(Guide base, Guide guide) {
        Guide merged = new Guide(
                guide.title() == null ? base.title() : guide.title(),
                guide.intro() == null ? base.intro() : guide.intro(),
                mergeLists(base.authors(), guide.authors()),
                guide.categories() == null ? base.categories() : guide.categories(),
                guide.publicationDate(),
                guide.minimumJavaVersion() == null ? base.minimumJavaVersion() : guide.minimumJavaVersion(),
                guide.maximumJavaVersion() == null ? base.maximumJavaVersion() : guide.maximumJavaVersion(),
                guide.cloud() == null ? base.cloud() : guide.cloud(),
                base.skipGradleTests() || guide.skipGradleTests(),
                base.skipMavenTests() || guide.skipMavenTests(),
                guide.asciidoctor(),
                guide.languages() == null ? base.languages() : guide.languages(),
                mergeLists(base.tags(), guide.tags()),
                guide.buildTools() == null ? base.buildTools() : guide.buildTools(),
                guide.testFramework() == null ? base.testFramework() : guide.testFramework(),
                guide.zipIncludes(),
                guide.slug(),
                guide.publish(),
                guide.base(),
                guide.env() == null ? base.env() : guide.env(),
                mergeApps(base.apps(),guide.apps())
        );
        return merged;
    }

    private static List<App> mergeApps(List<App> base, List<App> guide) {
        Map<String, App> baseApps = base.stream()
                .collect(Collectors.toMap(App::name, app -> app));

        Map<String, App> guideApps = guide.stream()
                .collect(Collectors.toMap(App::name, app -> app));

        Set<String> baseOnly = new HashSet<>(baseApps.keySet());
        baseOnly.removeAll(guideApps.keySet());

        Set<String> guideOnly = new HashSet<>(guideApps.keySet());
        guideOnly.removeAll(baseApps.keySet());

        Set<String> inBoth = new HashSet<>(baseApps.keySet());
        inBoth.retainAll(guideApps.keySet());

        List<App> merged = new ArrayList<>();
        merged.addAll(baseOnly.stream()
                .map(baseApps::get)
                .collect(Collectors.toList()));
        merged.addAll(guideOnly.stream()
                .map(guideApps::get)
                .collect(Collectors.toList()));

        for (String name : inBoth) {
            App baseApp = baseApps.get(name);
            App guideApp = guideApps.get(name);
            App mergedApp = new App(
                    guideApp.name(),
                    guideApp.packageName(),
                    guideApp.applicationType(),
                    guideApp.framework(),
                    mergeLists(guideApp.features(), baseApp.features()),
                    mergeLists(guideApp.invisibleFeatures(), baseApp.invisibleFeatures()),
                    mergeLists(guideApp.javaFeatures(), baseApp.javaFeatures()),
                    mergeLists(guideApp.kotlinFeatures(), baseApp.kotlinFeatures()),
                    mergeLists(guideApp.groovyFeatures(), baseApp.groovyFeatures()),
                    guideApp.testFramework(),
                    guideApp.excludeTest(),
                    baseApp.validateLicense()
            );
            merged.add(mergedApp);
        }

        return merged;
    }

    private static List mergeLists(Collection base, Collection others) {
        List merged = new ArrayList<>();
        if (base != null) {
            merged.addAll(base);
        }
        if (others != null) {
            merged.addAll(others);
        }
        return merged;
    }

    private static void addAllSafe(Collection target, Collection src) {
        if(target == null) {
            throw new NullPointerException("Target list cannot be null");
        }

        if(src != null) {
            target.addAll(src);
        }
    }
}
