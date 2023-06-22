package me.sasanqua.pradle.tasks

import me.sasanqua.pradle.tasks.utils.pythonCommand
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction
import org.gradle.kotlin.dsl.property

open class ExecuteZipAppTask : DefaultTask() {
    @Input
    val inputExecutable = project.objects.property<String>()

    @Input
    val inputZipApp = project.objects.property<String>()

    @TaskAction
    fun executeZipApp() {
        val executable = inputExecutable.get()
        val app = inputZipApp.get()
        project.exec { commandLine = pythonCommand(executable, app) }
    }

    companion object {
        const val NAME = "executeZipApp"
    }
}