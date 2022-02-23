package io.micronaut.guides

import groovy.transform.CompileStatic
import jakarta.inject.Inject
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.Property

@CompileStatic
class GuidesExtension {

    final Property<File> output

    @Inject
    GuidesExtension(ObjectFactory objects) {
        output = objects.property(File)
    }
}
