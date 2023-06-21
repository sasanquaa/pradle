package me.sasanqua.pradle.internal

import me.sasanqua.pradle.dependencies.PradleDependenciesConfiguration
import me.sasanqua.pradle.dependencies.PradleDependency

class DefaultPradleDependenciesConfiguration private constructor(private val dependencies: MutableSet<PradleDependency>) :
    PradleDependenciesConfiguration {

    override fun pip(name: String, version: String?) {
        dependencies.add(DefaultPradleDependency.Factory.create(name, version))
    }

    object Factory {
        fun create(dependencies: MutableSet<PradleDependency>) = DefaultPradleDependenciesConfiguration(dependencies)
    }
}