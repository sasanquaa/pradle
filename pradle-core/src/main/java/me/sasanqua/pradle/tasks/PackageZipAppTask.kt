package me.sasanqua.pradle.tasks

import me.sasanqua.pradle.PradleExtension
import me.sasanqua.pradle.tasks.utils.zipAppCommand
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.TaskAction
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.property

open class PackageZipAppTask : DefaultTask() {
    @Input
    val executable = project.objects.property<String>()

    @Internal
    val executableApp = project.objects.property<String>()

    @TaskAction
    fun packageZipApp() {
        val executable = executable.get()
        val zipAppDir = project.buildDir.resolve("zipApp")
        val zippAppExecutable = project.buildDir.resolve("zipApp.pyz").absolutePath
        val extension = project.extensions.getByType<PradleExtension>()
        val mainSourceSet = extension.sourceSets.main.get()

        project.delete {
            delete(zipAppDir)
        }
        project.copy {
            from(mainSourceSet.python, mainSourceSet.resources)
            exclude("python.iml")
            into(zipAppDir)
        }
        project.exec {
            commandLine = zipAppCommand(executable, zipAppDir.absolutePath, "-m", extension.appEntry)
            workingDir = project.buildDir
        }
        executableApp.set(zippAppExecutable)
    }

    companion object {
        const val NAME = "packageZipApp"
    }
}