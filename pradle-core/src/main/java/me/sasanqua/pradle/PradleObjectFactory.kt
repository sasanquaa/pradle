package me.sasanqua.pradle

import me.sasanqua.pradle.dependencies.PradleDependenciesConfiguration
import me.sasanqua.pradle.dependencies.PradleDependency
import me.sasanqua.pradle.dependencies.PradleSourceSet
import me.sasanqua.pradle.dependencies.PradleSourceSetContainer
import org.gradle.api.file.SourceDirectorySet
import org.gradle.api.model.ObjectFactory

interface PradleObjectFactory {
    fun extension(objectFactory: ObjectFactory): PradleExtension

    fun dependency(name: String, version: String?): PradleDependency

    fun dependenciesConfiguration(extension: PradleExtension): PradleDependenciesConfiguration

    fun sourceSet(
        name: String,
        python: SourceDirectorySet,
        resources: SourceDirectorySet
    ): PradleSourceSet

    fun sourceSetContainer(factory: ObjectFactory): PradleSourceSetContainer
}