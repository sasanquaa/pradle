package me.sasanqua.pradle.tasks

import me.sasanqua.pradle.PradleExtension
import me.sasanqua.pradle.dependencies.PradleDependency
import me.sasanqua.pradle.tasks.utils.PythonExecutableFinder
import me.sasanqua.pradle.tasks.utils.pipInstallRequirementsCommand
import me.sasanqua.pradle.tasks.utils.venvCommand
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.TaskAction
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.property
import java.io.File

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
        setupDependencies(virtualExecutable, requirements, dependencies)
    }

    private fun setupVenvFile() = project.buildDir.resolve(VENV).resolve(project.name)

    private fun setupEnvironment(executable: String, venv: File): String {
        venv.delete() // quick and easy, JUST DELETE THEM!
        project.exec {
            commandLine = venvCommand(executable, venv.absolutePath)
        }
        return PythonExecutableFinder.findFromVirtualEnvironment(venv).toString()
    }

    private fun setupDependencies(
        executable: String,
        requirements: File,
        dependencies: Set<PradleDependency>
    ) {
        requirements.writeText(dependencies.joinToString(System.lineSeparator()) { it.asPipString() })
        project.exec { commandLine = pipInstallRequirementsCommand(executable, requirements.absolutePath) }
    }

    companion object {
        private const val REQUIREMENTS = "requirements.txt"
        private const val VENV = "venv"
        const val NAME = "setupPythonEnvironment"
    }
}