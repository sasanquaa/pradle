package me.sasanqua.pradle

import me.sasanqua.pradle.dependencies.PradleSourceSet
import me.sasanqua.pradle.internal.DefaultPradleExtension
import me.sasanqua.pradle.tasks.PackagePexTask
import me.sasanqua.pradle.tasks.SetupPythonEnvironmentTask
import me.sasanqua.pradle.tasks.VerifyPythonTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.findByType
import org.gradle.kotlin.dsl.getByType
import org.gradle.plugins.ide.idea.model.IdeaModel
import java.io.File
import kotlin.io.path.Path

class PradlePlugin : Plugin<Project> {
    override fun apply(target: Project): Unit = with(target) {
        applyExtension()
        applyDefaultSourceSets()
        applyOptionalPlugins()
        applyTasks()
    }

    private fun Project.applyExtension() {
        extensions.create(
            PradleExtension::class,
            PradleExtension.NAME,
            DefaultPradleExtension::class,
            this
        )
    }

    private fun Project.applyDefaultSourceSets() {
        val sourceSets = extensions.getByType<PradleExtension>().sourceSets
        sourceSets.main.configure {
            python.srcDir(PradleSourceSet.DEFAULT_PYTHON_MAIN_PATH)
            resources.srcDir(PradleSourceSet.DEFAULT_RESOURCES_MAIN_PATH)
        }
        sourceSets.test.configure {
            python.srcDir(PradleSourceSet.DEFAULT_PYTHON_TEST_PATH)
            resources.srcDir(PradleSourceSet.DEFAULT_RESOURCES_TEST_PATH)
        }
    }

    private fun Project.applyOptionalPlugins() {
        val pradleExtension = extensions.getByType<PradleExtension>()
        val ideaExtension = extensions.findByType<IdeaModel>()
        val javaExtension = extensions.findByType<JavaPluginExtension>()

        javaExtension?.sourceSets?.forEach { javaSourceSet ->
            val sourceSetName = javaSourceSet.name
            val sourceName = "python"
            val sourceDir = Path("src").resolve(sourceSetName).resolve(sourceName)
            val pradleSourceSet = pradleExtension.sourceSets.maybeCreate(sourceSetName)
            pradleSourceSet.python.srcDir(sourceDir)
            pradleSourceSet.resources.srcDir(javaSourceSet.resources)
            javaSourceSet.extensions.add(sourceName, pradleSourceSet)
        }

        ideaExtension?.module {
            pradleExtension.sourceSets.flatMap { it.python.srcDirs }.forEach(sourceDirs::add)
            pradleExtension.sourceSets.flatMap { it.resources.srcDirs }.forEach(resourceDirs::add)
        }
    }

    private fun Project.applyTasks() {
        val verifyTask = tasks.create<VerifyPythonTask>(VerifyPythonTask.NAME)
        val environmentTask = tasks.create<SetupPythonEnvironmentTask>(SetupPythonEnvironmentTask.NAME) {
            inputExecutable.value(verifyTask.outputExecutable.asFile.map(File::readText))
        }
        tasks.create<PackagePexTask>(PackagePexTask.NAME) {
            inputExecutable.value(environmentTask.outputExecutable.asFile.map(File::readText))
        }
    }
}