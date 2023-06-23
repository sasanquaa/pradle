package me.sasanqua.pradle

import me.sasanqua.pradle.dependencies.PradleSourceSet
import me.sasanqua.pradle.tasks.ExecuteZipAppTask
import me.sasanqua.pradle.tasks.PackageZipAppTask
import me.sasanqua.pradle.tasks.SetupPythonEnvironmentTask
import me.sasanqua.pradle.tasks.VerifyPythonTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.add
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.findByType
import org.gradle.kotlin.dsl.getByType
import org.gradle.plugins.ide.idea.model.IdeaModel
import java.util.ServiceLoader

@Suppress("unused")
class PradlePlugin : Plugin<Project> {
    private val objectFactory: PradleObjectFactory by lazy {
        ServiceLoader.load(PradleObjectFactory::class.java).first()
    }

    override fun apply(target: Project): Unit = with(target) {
        applyExtension()
        applyDefaultSourceSets()
        applyOptionalPlugins()
        applyTasks()
    }

    private fun Project.applyExtension() {
        extensions.add(
            PradleExtension::class,
            PradleExtension.NAME,
            objectFactory.extension(project.objects)
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

        ideaExtension?.module {
            pradleExtension.sourceSets.main.map { it.python.srcDirs }.get().forEach(sourceDirs::add)
            pradleExtension.sourceSets.main.map { it.resources.srcDirs }.get().forEach(resourceDirs::add)
        }
    }

    private fun Project.applyTasks() {
        val verifyTask = tasks.create<VerifyPythonTask>(VerifyPythonTask.NAME)
        val environmentTask =
            tasks.create<SetupPythonEnvironmentTask>(SetupPythonEnvironmentTask.NAME, objectFactory).apply {
                dependsOn(verifyTask)
                executable.value(verifyTask.executable)
            }
        val packageTask = tasks.create<PackageZipAppTask>(PackageZipAppTask.NAME) {
            dependsOn(environmentTask)
            executable.value(environmentTask.virtualExecutable)
        }
        tasks.create<ExecuteZipAppTask>(ExecuteZipAppTask.NAME) {
            dependsOn(packageTask)
            executable.value(packageTask.executable)
            executableApp.value(packageTask.executableApp)
        }
    }
}