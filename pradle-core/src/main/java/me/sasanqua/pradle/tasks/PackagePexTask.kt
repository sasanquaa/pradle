package me.sasanqua.pradle.tasks

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction
import org.gradle.kotlin.dsl.property

open class PackagePexTask : DefaultTask() {
    @Input
    val inputExecutable = project.objects.property<String>()

    @TaskAction
    fun packagePex() {
        TODO()
    }

    companion object {
        const val NAME = "build"
    }
}