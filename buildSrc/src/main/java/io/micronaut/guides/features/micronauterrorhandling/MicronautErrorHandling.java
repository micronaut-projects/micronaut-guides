package io.micronaut.guides.features.micronauterrorhandling;

import edu.umd.cs.findbugs.annotations.NonNull;
import io.micronaut.guides.features.GuideFeature;
import io.micronaut.guides.features.GuidesUtils;
import io.micronaut.guides.features.micronauterrorhandling.templates.*;
import io.micronaut.starter.application.Project;
import io.micronaut.starter.application.generator.GeneratorContext;

import javax.inject.Singleton;

@Singleton
public class MicronautErrorHandling extends GuideFeature {

    @NonNull
    @Override
    public String getName() {
        return "micronaut-error-handling";
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        Project project = generatorContext.getProject();
        GuidesUtils.renderViewTemplate(generatorContext, "notFound.vm", notfoundvm.template());
        GuidesUtils.renderViewTemplate(generatorContext, "bookscreate.vm", bookscreatevm.template());
        GuidesUtils.renderSource(generatorContext, "NotFoundController", notfoundcontrollerJava.template(project));
        GuidesUtils.renderSource(generatorContext, "BookController", bookcontrollerJava.template(project));
        GuidesUtils.renderSource(generatorContext, "CommandBookSave", commandbooksaveJava.template(project));
        GuidesUtils.renderSource(generatorContext, "MessageSource", messagesourceJava.template(project));
        GuidesUtils.renderSource(generatorContext, "OutOfStockException", outofstockexception.template(project));
        GuidesUtils.renderSource(generatorContext, "OutOfStockExceptionHandler", outofstockexceptionhandlerJava.template(project));

        GuidesUtils.renderTest(generatorContext, "NotFoundSpec", notfoundspecGroovy.template(project));
        GuidesUtils.renderTest(generatorContext, "NotFoundPage", notfoundpageGroovy.template(project));
        GuidesUtils.renderTest(generatorContext, "ExceptionHandlerSpec", exceptionhandlerspecGroovy.template(project));
        GuidesUtils.renderTest(generatorContext, "BookCreatePage", bookcreatepageGroovy.template(project));
        GuidesUtils.renderTest(generatorContext, "BookCreateSpec", bookcreatespecGroovy.template(project));
    }
}
