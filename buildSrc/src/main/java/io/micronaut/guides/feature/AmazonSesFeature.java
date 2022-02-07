package io.micronaut.guides.feature;

import io.micronaut.context.annotation.Replaces;
import io.micronaut.starter.feature.email.AmazonSesEmailFeature;
import io.micronaut.starter.feature.email.TemplateEmailFeature;
import jakarta.inject.Singleton;

// https://github.com/micronaut-projects/micronaut-starter/pull/1064
@Replaces(AmazonSesEmailFeature.class)
@Singleton
public class AmazonSesFeature extends AmazonSesEmailFeature {
    public AmazonSesFeature(TemplateEmailFeature templateEmailFeature) {
        super(templateEmailFeature);
    }


    @Override
    public String getModule() {
        return "amazon-ses";
    }

}
