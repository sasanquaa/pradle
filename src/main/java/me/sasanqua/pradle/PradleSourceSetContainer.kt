package me.sasanqua.pradle

import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.NamedDomainObjectProvider

interface PradleSourceSetContainer : NamedDomainObjectContainer<PradleSourceSet> {
    val main: NamedDomainObjectProvider<PradleSourceSet>
    val test: NamedDomainObjectProvider<PradleSourceSet>
}