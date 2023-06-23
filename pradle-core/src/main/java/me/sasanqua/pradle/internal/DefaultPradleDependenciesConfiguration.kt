package me.sasanqua.pradle.internal

import me.sasanqua.pradle.dependencies.PradleDependenciesConfiguration

class DefaultPradleDependenciesConfiguration(
    private val dependencies: MutableSet<DefaultPradleDependency>,
    private val objectFactory: DefaultPradleObjectFactory
) : PradleDependenciesConfiguration {
    override fun pip(name: String, version: String?) {
        dependencies.add(objectFactory.dependency(name, version))
    }
}