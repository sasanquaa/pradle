package me.sasanqua.pradle.internal

import me.sasanqua.pradle.PradleExtension
import me.sasanqua.pradle.dependencies.PradleDependenciesConfiguration
import me.sasanqua.pradle.dependencies.PradleSourceSet
import me.sasanqua.pradle.dependencies.PradleSourceSetContainer
import org.gradle.api.Action
import org.gradle.api.model.ObjectFactory
import org.gradle.kotlin.dsl.domainObjectContainer

class DefaultPradleExtension(factory: ObjectFactory) : PradleExtension {
    override val sourceSets: PradleSourceSetContainer = DefaultPradleSourceSetContainer(factory.domainObjectContainer(PradleSourceSet::class))

    override fun sourceSets(action: Action<PradleSourceSetContainer>) = action.execute(sourceSets)

    override fun dependencies(action: Action<PradleDependenciesConfiguration>) {
        TODO("Not yet implemented")
    }
}