package me.sasanqua.pradle.tasks

import me.sasanqua.pradle.PradleExtension
import me.sasanqua.pradle.PradleObjectFactory
import me.sasanqua.pradle.dependencies.PradleDependency
import me.sasanqua.pradle.dependencies.PradleDependency.Companion.PIP_SEPARATOR
import me.sasanqua.pradle.tasks.utils.PythonExecutableFinder
import me.sasanqua.pradle.tasks.utils.pipInstallRequirementsCommand
import me.sasanqua.pradle.tasks.utils.pipUninstallRequirementsCommand
import me.sasanqua.pradle.tasks.utils.venvCommand
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.TaskAction
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.property
import java.io.File
import javax.inject.Inject

open class SetupPythonEnvironmentTask @Inject constructor(private val objectFactory: PradleObjectFactory) :
    DefaultTask() {
    @Input
    val executable = project.objects.property<String>()

    @Internal
    val virtualExecutable = project.objects.property<String>()

    @TaskAction
    fun setup() {
        val executablePath = executable.get()
        val dependencies = project.extensions.getByType<PradleExtension>().dependencies
        val venv = setupVenvFile()
        val requirements = venv.resolve(REQUIREMENTS).apply { createNewFile() }

        val virtualExecutablePath = setupEnvironment(executablePath, venv)
        val unusedRequirements = setupRequirements(requirements, dependencies)
        setupDependencies(virtualExecutablePath, requirements, unusedRequirements)
        virtualExecutable.set(virtualExecutablePath)
    }

    private fun setupVenvFile() = project.buildDir.resolve(VENV)

    private fun setupEnvironment(executable: String, venv: File): String {
        project.exec {
            commandLine = venvCommand(executable, venv.absolutePath)
        }
        return PythonExecutableFinder.findFromVirtualEnvironment(venv).toString()
    }

    private fun setupRequirements(requirements: File, dependencies: Set<PradleDependency>): File {
        val newDependencies = dependencies.associateBy { it.name }.toMutableMap()
        val oldDependencies = requirements.readLines().associate {
            val split = it.split(PIP_SEPARATOR)
            val dependency = objectFactory.dependency(split[0], split.elementAtOrNull(1))
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