package me.sasanqua.pradle.tasks

import me.sasanqua.pradle.PradleExtension
import me.sasanqua.pradle.tasks.utils.PythonExecutableFinder
import me.sasanqua.pradle.tasks.utils.checkVenvCommand
import me.sasanqua.pradle.tasks.utils.getVersionCommand
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction
import org.gradle.kotlin.dsl.getByType
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.StringReader

open class VerifyPythonTask : DefaultTask() {
    private val outputExecutableDefault: File by lazy {
        File.createTempFile("python_executable", null)
    }

    @OutputFile
    val outputExecutable = project.objects.fileProperty().convention { outputExecutableDefault }

    @TaskAction
    fun verify() {
        val executable =
            checkNotNull(PythonExecutableFinder.findFromPath()?.absolutePath) { "Unable to find Python executable" }
        val pythonVersion = project.extensions.getByType<PradleExtension>().version
        verifyPythonVersion(executable, pythonVersion)
        verifyVenvAvailable(executable)
        outputExecutable.get().asFile.writeText(executable)
    }

    private fun verifyPythonVersion(executable: String, version: String) {
        val split = version.split(".")
        val major = checkNotNull(split.elementAtOrNull(0)) { "Python major version missing" }.toInt()
        val minor = split.elementAtOrNull(1)?.toInt() ?: 3
        val patch = split.elementAtOrNull(2)?.toInt() ?: 0
        val outputStream = ByteArrayOutputStream()

        check(major >= 3 && minor >= 3) { "Only python 3.3 and above are supported" }

        project.exec {
            commandLine = getVersionCommand(executable)
            standardOutput = outputStream
        }

        val executableSplit = StringReader(outputStream.toString()).readLines().first().split(".")
        val executableMajor = executableSplit[0].toInt()
        val executableMinor = executableSplit[1].toInt()
        val executablePatch = executableSplit[2].toInt()

        check(executableMajor >= major) { "Require python major $major but $executableMajor found" }
        check(executableMinor >= minor) { "Require python minor $minor but $executableMinor found" }
        check(executablePatch >= patch) { "Require python patch $patch but $executablePatch found" }
    }

    private fun verifyVenvAvailable(executable: String) {
        val result = project.exec {
            commandLine = checkVenvCommand(executable)
            isIgnoreExitValue = true
        }
        check(result.exitValue == 0) { "Package venv not found" }
    }

    companion object {
        const val NAME = "verifyPython"
    }
}