package me.sasanqua.pradle.tasks

import me.sasanqua.pradle.tasks.utils.pythonCommand
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction
import org.gradle.kotlin.dsl.property

open class ExecuteZipAppTask : DefaultTask() {
    @Input
    val executable = project.objects.property<String>()

    @Input
    val executableApp = project.objects.property<String>()

    @TaskAction
    fun executeZipApp() {
        val executable = executable.get()
        val app = executableApp.get()
        project.exec { commandLine = pythonCommand(executable, app) }
    }

    companion object {
        const val NAME = "executeZipApp"
    }
}