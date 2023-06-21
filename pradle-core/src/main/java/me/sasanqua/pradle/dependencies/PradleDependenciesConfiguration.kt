package me.sasanqua.pradle.dependencies

interface PradleDependenciesConfiguration {
    fun pip(name: String, version: String? = null)
}