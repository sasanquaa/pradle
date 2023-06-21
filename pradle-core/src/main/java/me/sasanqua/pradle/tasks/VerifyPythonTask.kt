package me.sasanqua.pradle.tasks

import me.sasanqua.pradle.tasks.utils.PythonExecutableFinder
import me.sasanqua.pradle.tasks.utils.checkVenvCommand
import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction
import java.io.File

open class VerifyPythonTask : DefaultTask() {
    private val outputExecutableDefault: File by lazy {
        File.createTempFile("python_executable", null)
    }

    @OutputFile
    val outputExecutable = project.objects.fileProperty().convention { outputExecutableDefault }

    @TaskAction
    fun verify() {
        val executable =
            PythonExecutableFinder.findFromPath()?.absolutePath ?: throw GradleException("Unable to find Python executable")
        verifyPythonVersion(executable)
        verifyVenvAvailable(executable)
        outputExecutable.get().asFile.writeText(executable)
    }

    private fun verifyPythonVersion(executable: String) {
        // TODO
    }

    private fun verifyVenvAvailable(executable: String) {
        val result = project.exec {
            commandLine = checkVenvCommand(executable)
            isIgnoreExitValue = true
        }
        if (result.exitValue != 0) {
            throw GradleException("Package 'venv' not found")
        }
    }

    companion object {
        const val NAME = "verifyPython"
    }
}