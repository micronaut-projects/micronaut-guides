from java.time import LocalDate
from micronaut.http.annotation import Controller, Get

from .build_tool import BuildTool
from .guide import Guide
from .language import Language
from .option import Option


@Controller("/latest")  # <1>
class LatestGuidesController:

    GUIDES = [
        Guide(
            "Creating your first Micronaut application",
            "Learn how to create a Hello World Micronaut application with a controller and a functional test.",
            ["Ivan Lopez", "Sergio del Amo"],
            ["junit", "getting_started", "graalvm"],
            ["Getting Started"],
            LocalDate.of(2018, 5, 23),
            "creating-your-first-micronaut-app",
            "https://guides.micronaut.io/latest/creating-your-first-micronaut-app.html",
            [
                Option(
                    Language.JAVA,
                    BuildTool.GRADLE,
                    "https://guides.micronaut.io/latest/creating-your-first-micronaut-app-gradle-java.html",
                ),
                Option(
                    Language.GROOVY,
                    BuildTool.GRADLE,
                    "https://guides.micronaut.io/latest/creating-your-first-micronaut-app-gradle-groovy.html",
                ),
                Option(
                    Language.KOTLIN,
                    BuildTool.GRADLE,
                    "https://guides.micronaut.io/latest/creating-your-first-micronaut-app-gradle-kotlin.html",
                ),
                Option(
                    Language.JAVA,
                    BuildTool.MAVEN,
                    "https://guides.micronaut.io/latest/creating-your-first-micronaut-app-maven-java.html",
                ),
                Option(
                    Language.GROOVY,
                    BuildTool.MAVEN,
                    "https://guides.micronaut.io/latest/creating-your-first-micronaut-app-maven-groovy.html",
                ),
                Option(
                    Language.KOTLIN,
                    BuildTool.MAVEN,
                    "https://guides.micronaut.io/latest/creating-your-first-micronaut-app-maven-kotlin.html",
                ),
                Option(
                    Language.PYTHON,
                    BuildTool.PYRONAUT,
                    "https://guides.micronaut.io/latest/creating-your-first-micronaut-app-pyronaut-python.html",
                ),
            ],
        )
    ]

    @Get("/guides.json")  # <2>
    def latest_guides(self) -> list[Guide]:
        return self.GUIDES
