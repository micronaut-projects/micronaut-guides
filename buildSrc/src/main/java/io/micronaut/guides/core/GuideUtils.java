package io.micronaut.guides.core;

import java.util.*;
import java.util.stream.Collectors;

public final class GuideUtils {

    private GuideUtils() {}

    public static Guide merge(Guide base, Guide guide) {
        guide.authors().addAll(base.authors());
        guide.tags().addAll(base.tags());
        Guide merged = new Guide(
                guide.title() == null ? base.title() : guide.title(),
                guide.intro() == null ? base.intro() : guide.intro(),
                guide.authors(),
                guide.categories() == null ? base.categories() : guide.categories(),
                guide.publicationDate(),
                guide.minimumJavaVersion() == null ? base.minimumJavaVersion() : guide.minimumJavaVersion(),
                guide.maximumJavaVersion() == null ? base.maximumJavaVersion() : guide.maximumJavaVersion(),
                guide.cloud() == null ? base.cloud() : guide.cloud(),
                base.skipGradleTests() || guide.skipGradleTests(),
                base.skipMavenTests() || guide.skipMavenTests(),
                guide.asciidoctor(),
                guide.languages() == null ? base.languages() : guide.languages(),
                guide.tags(),
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
            guideApp.features().addAll(baseApp.features());
            guideApp.invisibleFeatures().addAll(baseApp.invisibleFeatures());
            guideApp.javaFeatures().addAll(baseApp.javaFeatures());
            guideApp.kotlinFeatures().addAll(baseApp.kotlinFeatures());
            App mergedApp = new App(
                    guideApp.name(),
                    guideApp.packageName(),
                    guideApp.applicationType(),
                    guideApp.framework(),
                    guideApp.features(),
                    guideApp.invisibleFeatures(),
                    guideApp.kotlinFeatures(),
                    guideApp.javaFeatures(),
                    guideApp.testFramework(),
                    guideApp.excludeTest(),
                    baseApp.validateLicense()
            );
            merged.add(mergedApp);
        }

        return merged;

    }
}
