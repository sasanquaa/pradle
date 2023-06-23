package me.sasanqua.pradle.internal

import me.sasanqua.pradle.dependencies.PradleDependency

data class DefaultPradleDependency(
    override val name: String,
    override val version: String?
) : PradleDependency