package io.micronaut.guides

import groovy.transform.CompileStatic
import io.micronaut.starter.rocker.plugin.RockerPlugin
import org.gradle.api.Action
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.plugins.BasePlugin

@CompileStatic
class GuidesPlugin implements Plugin<Project> {

    static final String TASK_GEN_GUIDES = 'generateGuides'
    static final String TASK_BUILD = 'build'
    static final String GROUP_MICRONAUT = 'micronaut'
    static final String EXTENSION_NAME = 'guides'

    @Override
    void apply(Project project) {
        /*project.getPlugins().apply(BasePlugin.class)
        //project.getPlugins().apply(RockerPlugin.class)
        project.extensions.create(EXTENSION_NAME, GuidesExtension)

        project.tasks.named(TASK_BUILD).configure(new Action<Task>() {
            @Override
            void execute(Task task) {
                task.dependsOn(TASK_GEN_GUIDES)
            }
        })*/
    }

}
