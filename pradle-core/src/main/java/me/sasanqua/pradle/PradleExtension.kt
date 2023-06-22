package me.sasanqua.pradle

import me.sasanqua.pradle.dependencies.PradleDependenciesConfiguration
import me.sasanqua.pradle.dependencies.PradleDependency
import me.sasanqua.pradle.dependencies.PradleSourceSetContainer
import org.gradle.api.Action

interface PradleExtension {
    val sourceSets: PradleSourceSetContainer
    val dependencies: Set<PradleDependency>
    var appEntry: String
    var version: String

    fun sourceSets(action: Action<PradleSourceSetContainer>)

    fun dependencies(action: Action<PradleDependenciesConfiguration>)

    companion object {
        const val MIN_PYTHON_VERSION_MAJOR = 3
        const val MIN_PYTHON_VERSION_MINOR = 5
        const val MIN_PYTHON_VERSION = "$MIN_PYTHON_VERSION_MAJOR.$MIN_PYTHON_VERSION_MINOR"
        const val DEFAULT_APP_ENTRY = "app:main"
        const val NAME = "pradle"
    }
}