package io.micronaut.guides.features;

import com.fizzed.rocker.RockerModel;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.template.RockerTemplate;

public class GuidesUtils {
    static final String RESOURCES = "src/main/resources";
    public static final String VIEWS = "views";

    public static void renderViewTemplate(GeneratorContext generatorContext, String fileName, RockerModel rockerModel) {
        String templateName = RESOURCES  + "/" + VIEWS + "/" + fileName;
        generatorContext.addTemplate(fileName + "ViewTemplate", new RockerTemplate(templateName, rockerModel));
    }

    public static void renderTest(GeneratorContext generatorContext, String fileNameWithoutSuffix, RockerModel rockerModel) {
        String src = generatorContext.getTestSourcePath("/{packagePath}/" + fileNameWithoutSuffix);
        generatorContext.addTemplate(fileNameWithoutSuffix + "TestTemplate", new RockerTemplate(src, rockerModel));
    }

    public static void renderSource(GeneratorContext generatorContext, String fileNameWithoutSuffix, RockerModel rockerModel) {
        String src = generatorContext.getSourcePath("/{packagePath}/" + fileNameWithoutSuffix);
        generatorContext.addTemplate(fileNameWithoutSuffix + "SourceTemplate", new RockerTemplate(src, rockerModel));
    }
}
