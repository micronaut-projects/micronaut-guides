package example.micronaut;

import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@Controller("/latest") // <1>
class LatestGuidesController {

    private static final List<Guide> GUIDES = Collections.singletonList(
            new Guide("Creating your first Micronaut application",
                    "Learn how to create a Hello World Micronaut application with a controller and a functional test.",
                    List.of("Iván López", "Sergio dle Amo"),
                    List.of("junit","getting_started","graalvm"),
                    List.of("Getting Started"),
                    LocalDate.of(2018, 5, 23),
                    "creating-your-first-micronaut-app",
                    "https://guides.micronaut.io/latest/creating-your-first-micronaut-app.html",
                    List.of(new Option(Language.JAVA, BuildTool.GRADLE, "https://guides.micronaut.io/latest/creating-your-first-micronaut-app-gradle-java.html"),
                            new Option(Language.GROOVY, BuildTool.GRADLE, "https://guides.micronaut.io/latest/creating-your-first-micronaut-app-gradle-groovy.html"),
                            new Option(Language.KOTLIN, BuildTool.GRADLE, "https://guides.micronaut.io/latest/creating-your-first-micronaut-app-gradle-kotlin.html"),
                            new Option(Language.JAVA, BuildTool.MAVEN, "https://guides.micronaut.io/latest/creating-your-first-micronaut-app-maven-java.html"),
                            new Option(Language.GROOVY, BuildTool.MAVEN, "https://guides.micronaut.io/latest/creating-your-first-micronaut-app-maven-groovy.html"),
                            new Option(Language.KOTLIN, BuildTool.MAVEN, "https://guides.micronaut.io/latest/creating-your-first-micronaut-app-maven-kotlin.html")
                            ))
    );

    @Get("/guides.json") // <2>
    List<Guide> latestGuides() {
        return GUIDES;
    }
}
