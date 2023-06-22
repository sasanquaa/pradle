package me.sasanqua.pradle.tasks

import me.sasanqua.pradle.PradleExtension
import me.sasanqua.pradle.dependencies.PradleDependency
import me.sasanqua.pradle.dependencies.PradleDependency.Companion.PIP_SEPARATOR
import me.sasanqua.pradle.tasks.utils.PythonExecutableFinder
import me.sasanqua.pradle.tasks.utils.pipInstallRequirementsCommand
import me.sasanqua.pradle.tasks.utils.pipUninstallRequirementsCommand
import me.sasanqua.pradle.tasks.utils.venvCommand
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.property
import java.io.File
import me.sasanqua.pradle.internal.DefaultPradleDependency.Factory as PradleDependencyFactory

open class SetupPythonEnvironmentTask : DefaultTask() {
    private val outputExecutableDefault: File by lazy {
        File.createTempFile("virtual_python_executable", null)
    }

    @Input
    val inputExecutable = project.objects.property<String>()

    @OutputFile
    val outputExecutable = project.objects.fileProperty().convention { outputExecutableDefault }

    @TaskAction
    fun setup() {
        val executable = inputExecutable.get()
        val dependencies = project.extensions.getByType<PradleExtension>().dependencies
        val venv = setupVenvFile()
        val requirements = venv.resolve(REQUIREMENTS).apply { createNewFile() }

        val virtualExecutable = setupEnvironment(executable, venv)
        val unusedRequirements = setupRequirements(requirements, dependencies)
        setupDependencies(virtualExecutable, requirements, unusedRequirements)
        outputExecutable.get().asFile.writeText(virtualExecutable)
    }

    private fun setupVenvFile() = project.buildDir.resolve(VENV)

    private fun setupEnvironment(executable: String, venv: File): String {
        project.exec {
            commandLine = venvCommand(executable, venv.absolutePath)
        }
        return PythonExecutableFinder.findFromVirtualEnvironment(venv).toString()
    }

    private fun setupRequirements(requirements: File, dependencies: Set<PradleDependency>): File {
        val newDependencies = dependencies.associate {
            it.name to PradleDependencyFactory.from(it)
        }.toMutableMap()
        val oldDependencies = requirements.readLines().associate {
            val split = it.split(PIP_SEPARATOR)
            val dependency = PradleDependencyFactory.create(split[0], split.elementAtOrNull(1))
            dependency.name to dependency
        }
        val intersectDependencies = oldDependencies.filterKeys { newDependencies.containsKey(it) }

        for ((key, value) in intersectDependencies) {
            newDependencies[key] = value
        }

        val finalDependencies = newDependencies.values.toSet()
        val unusedDependencies = oldDependencies.values.subtract(finalDependencies)
        val unusedRequirements = File.createTempFile(REQUIREMENTS, null)

        requirements.writeText(finalDependencies.joinToString(System.lineSeparator()) { it.asPipString() })
        unusedRequirements.writeText(unusedDependencies.joinToString(System.lineSeparator()) { it.asPipString() })
        return unusedRequirements
    }

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