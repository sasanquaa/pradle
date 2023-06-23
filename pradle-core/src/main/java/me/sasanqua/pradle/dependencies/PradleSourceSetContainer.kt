package me.sasanqua.pradle.dependencies

import org.gradle.api.NamedDomainObjectProvider

interface PradleSourceSetContainer {
    val main: NamedDomainObjectProvider<out PradleSourceSet>
    val test: NamedDomainObjectProvider<out PradleSourceSet>
}