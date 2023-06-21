package me.sasanqua.pradle.internal

import me.sasanqua.pradle.dependencies.PradleSourceSet
import org.gradle.api.NamedDomainObjectFactory
import org.gradle.api.file.SourceDirectorySet
import org.gradle.api.model.ObjectFactory

class DefaultPradleSourceSet private constructor(
    override val name: String,
    override val python: SourceDirectorySet,
    override val resources: SourceDirectorySet
) : PradleSourceSet {
    class Factory(private val factory: ObjectFactory) : NamedDomainObjectFactory<PradleSourceSet> {
        override fun create(name: String) = DefaultPradleSourceSet(
            name,
            factory.sourceDirectorySet(PYTHON, PYTHON_DISPLAY),
            factory.sourceDirectorySet(RESOURCES, RESOURCES_DISPLAY)
        )

    }

    private companion object {
        const val PYTHON = "python"
        const val PYTHON_DISPLAY = "Pradle Python Sources"
        const val RESOURCES = "resources"
        const val RESOURCES_DISPLAY = "Pradle Resources"
    }
}