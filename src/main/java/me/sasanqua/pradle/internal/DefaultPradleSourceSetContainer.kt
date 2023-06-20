package me.sasanqua.pradle.internal

import me.sasanqua.pradle.dependencies.PradleSourceSet
import me.sasanqua.pradle.dependencies.PradleSourceSetContainer
import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.NamedDomainObjectProvider

class DefaultPradleSourceSetContainer(delegate: NamedDomainObjectContainer<PradleSourceSet>) : PradleSourceSetContainer, NamedDomainObjectContainer<PradleSourceSet> by delegate {
    override val main: NamedDomainObjectProvider<PradleSourceSet> = delegate.register(MAIN)
    override val test: NamedDomainObjectProvider<PradleSourceSet> = delegate.register(TEST)

    private companion object {
        const val MAIN = "main"
        const val TEST = "test"
    }
}