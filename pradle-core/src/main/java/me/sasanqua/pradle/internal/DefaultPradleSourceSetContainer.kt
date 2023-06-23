package me.sasanqua.pradle.internal

import me.sasanqua.pradle.dependencies.PradleSourceSetContainer
import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.NamedDomainObjectProvider

private typealias DelegatedContainer = NamedDomainObjectContainer<DefaultPradleSourceSet>
private typealias DelegatedProvider = NamedDomainObjectProvider<DefaultPradleSourceSet>

class DefaultPradleSourceSetContainer(delegate: DelegatedContainer) : PradleSourceSetContainer {
    override val main: DelegatedProvider = delegate.register(MAIN)
    override val test: DelegatedProvider = delegate.register(TEST)

    private companion object {
        const val MAIN = "main"
        const val TEST = "test"
    }
}