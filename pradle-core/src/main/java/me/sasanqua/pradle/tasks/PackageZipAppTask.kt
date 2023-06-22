package me.sasanqua.pradle.tasks

import me.sasanqua.pradle.PradleExtension
import me.sasanqua.pradle.tasks.utils.zipAppCommand
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.property
import java.io.File

open class PackageZipAppTask : DefaultTask() {
    private val outputZipAppDefault: File by lazy {
        File.createTempFile("zip_app", null)
    }
    private val outputExecutableDefault: File by lazy {
        File.createTempFile("virtual_python_executable", null)
    }


    @Input
    val inputExecutable = project.objects.property<String>()

    @OutputFile
    val outputZipApp = project.objects.fileProperty().convention { outputZipAppDefault }

    @OutputFile
    val outputExecutable = project.objects.fileProperty().convention { outputExecutableDefault }

    @TaskAction
    fun packageZipApp() {
        val executable = inputExecutable.get()
        val zipAppDir = project.buildDir.resolve("zipApp")
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

        outputZipApp.get().asFile.writeText(project.buildDir.resolve("zipApp.pyz").absolutePath)
        outputExecutable.get().asFile.writeText(executable)
    }

    companion object {
        const val NAME = "packageZipApp"
    }
}