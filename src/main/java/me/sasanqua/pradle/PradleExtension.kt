package me.sasanqua.pradle

import org.gradle.api.Action

interface PradleExtension {
    val sourceSets: PradleSourceSetContainer

    fun sourceSets(action: Action<PradleSourceSetContainer>)

    companion object {
        const val NAME = "pradle"
    }
}