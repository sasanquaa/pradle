package me.sasanqua.pradle.dependencies

interface PradleDependency {
    val name: String
    val version: String?

    fun asPipString() = if (version == null) {
        name
    } else {
        "$name$PIP_SEPARATOR$version"
    }

    companion object {
        const val PIP_SEPARATOR = "=="
    }
}