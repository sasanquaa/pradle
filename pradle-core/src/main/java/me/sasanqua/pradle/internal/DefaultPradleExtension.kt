package me.sasanqua.pradle.internal

import me.sasanqua.pradle.PradleExtension
import me.sasanqua.pradle.PradleExtension.Companion.DEFAULT_APP_ENTRY
import me.sasanqua.pradle.PradleExtension.Companion.MIN_PYTHON_VERSION
import me.sasanqua.pradle.dependencies.PradleDependenciesConfiguration
import me.sasanqua.pradle.dependencies.PradleSourceSetContainer
import org.gradle.api.Action
import org.gradle.api.model.ObjectFactory

class DefaultPradleExtension(
    objectFactory: ObjectFactory,
    private val pradleObjectFactory: DefaultPradleObjectFactory
) : PradleExtension {
    override val sourceSets: DefaultPradleSourceSetContainer = pradleObjectFactory.sourceSetContainer(objectFactory)
    override val dependencies: MutableSet<DefaultPradleDependency> = mutableSetOf()
    override var appEntry: String = DEFAULT_APP_ENTRY
    override var version: String = MIN_PYTHON_VERSION

    override fun sourceSets(action: Action<PradleSourceSetContainer>) = action.execute(sourceSets)

    override fun dependencies(action: Action<PradleDependenciesConfiguration>) {
        val configuration = pradleObjectFactory.dependenciesConfiguration(this)
        action.execute(configuration)
    }
}