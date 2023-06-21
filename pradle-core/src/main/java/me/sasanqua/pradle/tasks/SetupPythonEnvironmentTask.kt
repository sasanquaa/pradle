package me.sasanqua.pradle.tasks

import me.sasanqua.pradle.PradleExtension
import me.sasanqua.pradle.dependencies.PradleDependency
import me.sasanqua.pradle.dependencies.PradleDependency.Companion.PIP_SEPARATOR
import me.sasanqua.pradle.internal.DefaultPradleDependency
import me.sasanqua.pradle.tasks.utils.*
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.TaskAction
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.property
import java.io.File
import java.io.FileOutputStream

open class SetupPythonEnvironmentTask : DefaultTask() {
    @InputFile
    val inputExecutable = project.objects.property<String>()

    @TaskAction
    fun setup() {
        val executable = inputExecutable.get()
        val dependencies = project.extensions.getByType<PradleExtension>().dependencies
        val venv = setupVenvFile()
        val requirements = venv.resolve(REQUIREMENTS)

        val virtualExecutable = setupEnvironment(executable, venv)
        val unusedRequirements = setupRequirements(virtualExecutable, requirements, dependencies)
        setupDependencies(virtualExecutable, requirements, unusedRequirements)
    }

    private fun setupVenvFile() = project.buildDir.resolve(VENV).resolve(project.name)

    private fun setupEnvironment(executable: String, venv: File): String {
        project.exec {
            commandLine = venvCommand(executable, venv.absolutePath)
        }
        return PythonExecutableFinder.findFromVirtualEnvironment(venv).toString()
    }

    private fun setupRequirements(
        executable: String,
        requirements: File,
        dependencies: Set<PradleDependency>
    ): File {
        val tempRequirements = File.createTempFile(REQUIREMENTS, null).also {
            project.exec {
                commandLine = pipFreezeCommand(executable)
                standardOutput = FileOutputStream(it)
            }
        }
        val newDependencies = dependencies.toMutableSet()
        val oldDependencies = tempRequirements.readLines().map {
            val split = it.split(PIP_SEPARATOR)
            DefaultPradleDependency.Factory.create(split[0], split.elementAtOrNull(1))
        }.toSet()
        val finalDependencies =
            newDependencies.subtract(oldDependencies) + newDependencies.intersect(oldDependencies)
        val unusedDependencies = oldDependencies.subtract(finalDependencies)

        tempRequirements.writeText(unusedDependencies.joinToString(System.lineSeparator()) { it.asPipString() })
        requirements.writeText(finalDependencies.joinToString(System.lineSeparator()) { it.asPipString() })
        return tempRequirements
    }

    // TODO: This doesn't really makes sense
    // TODO: since for dependencies with no version it will just uninstall and reinstall
    // TODO: and... pip freeze includes more dependencies than what is defined in build.gradle.kts
    private fun setupDependencies(executable: String, requirements: File, unusedRequirements: File) {
        if (unusedRequirements.length() > 0) {
            project.exec { commandLine = pipUninstallRequirementsCommand(executable, unusedRequirements.absolutePath) }
        }
        project.exec { commandLine = pipInstallRequirementsCommand(executable, requirements.absolutePath) }
    }

    companion object {
        private const val REQUIREMENTS = "requirements.txt"
        private const val VENV = "venv"
        const val NAME = "setupPythonEnvironment"
    }
}