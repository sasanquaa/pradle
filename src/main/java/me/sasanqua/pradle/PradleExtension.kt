package me.sasanqua.pradle

import me.sasanqua.pradle.dependencies.PradleDependenciesConfiguration
import me.sasanqua.pradle.dependencies.PradleSourceSetContainer
import org.gradle.api.Action

interface PradleExtension {
    val sourceSets: PradleSourceSetContainer

    fun sourceSets(action: Action<PradleSourceSetContainer>)

    fun dependencies(action: Action<PradleDependenciesConfiguration>)

    companion object {
        const val NAME = "pradle"
    }
}