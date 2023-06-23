package me.sasanqua.pradle.internal

import me.sasanqua.pradle.PradleExtension
import me.sasanqua.pradle.PradleObjectFactory
import org.gradle.api.NamedDomainObjectFactory
import org.gradle.api.file.SourceDirectorySet
import org.gradle.api.model.ObjectFactory
import org.gradle.kotlin.dsl.domainObjectContainer

class DefaultPradleObjectFactory : PradleObjectFactory {
    override fun extension(objectFactory: ObjectFactory): DefaultPradleExtension =
        DefaultPradleExtension(objectFactory, this)

    override fun dependency(name: String, version: String?): DefaultPradleDependency =
        DefaultPradleDependency(name, version)

    override fun dependenciesConfiguration(extension: PradleExtension): DefaultPradleDependenciesConfiguration =
        DefaultPradleDependenciesConfiguration((extension as DefaultPradleExtension).dependencies, this)

    override fun sourceSet(
        name: String,
        python: SourceDirectorySet,
        resources: SourceDirectorySet
    ): DefaultPradleSourceSet = DefaultPradleSourceSet(name, python, resources)

    override fun sourceSetContainer(factory: ObjectFactory): DefaultPradleSourceSetContainer {
        val elementFactory = NamedDomainObjectFactory<DefaultPradleSourceSet> {
            sourceSet(
                it,
                factory.sourceDirectorySet(PYTHON, PYTHON_DISPLAY),
                factory.sourceDirectorySet(RESOURCES, RESOURCES_DISPLAY)
            )
        }
        val delegate = factory.domainObjectContainer(
            DefaultPradleSourceSet::class,
            elementFactory
        )
        return DefaultPradleSourceSetContainer(delegate)
    }


    private companion object {
        const val PYTHON = "python"
        const val PYTHON_DISPLAY = "Pradle Python Sources"
        const val RESOURCES = "resources"
        const val RESOURCES_DISPLAY = "Pradle Resources"
    }
}