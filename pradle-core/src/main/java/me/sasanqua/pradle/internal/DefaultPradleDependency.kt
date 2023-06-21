package me.sasanqua.pradle.internal

import me.sasanqua.pradle.dependencies.PradleDependency

data class DefaultPradleDependency /*private constructor*/(
    override val name: String,
    override val version: String?
) : PradleDependency {
    object Factory {
        fun create(name: String, version: String?) = DefaultPradleDependency(name, version)

        fun from(dependency: PradleDependency) =
            dependency as? DefaultPradleDependency ?: create(dependency.name, dependency.version)
    }
}