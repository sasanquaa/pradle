package me.sasanqua.pradle.internal

import me.sasanqua.pradle.dependencies.PradleSourceSet
import me.sasanqua.pradle.dependencies.PradleSourceSetContainer
import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.NamedDomainObjectProvider
import org.gradle.api.model.ObjectFactory
import org.gradle.kotlin.dsl.domainObjectContainer

class DefaultPradleSourceSetContainer private constructor(delegate: NamedDomainObjectContainer<PradleSourceSet>) :
    PradleSourceSetContainer, NamedDomainObjectContainer<PradleSourceSet> by delegate {
    override val main: NamedDomainObjectProvider<PradleSourceSet> = delegate.register(MAIN)
    override val test: NamedDomainObjectProvider<PradleSourceSet> = delegate.register(TEST)

    object Factory {
        fun create(factory: ObjectFactory): DefaultPradleSourceSetContainer {
            val delegate = factory.domainObjectContainer(
                PradleSourceSet::class,
                DefaultPradleSourceSet.Factory(factory)
            )
            return DefaultPradleSourceSetContainer(delegate)
        }
    }

    private companion object {
        const val MAIN = "main"
        const val TEST = "test"
    }
}